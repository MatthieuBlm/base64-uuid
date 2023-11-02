package org.bellamy.base64_uuid;

import java.io.Serial;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.UUID;

public final class B64UUID implements Serializable, Comparable<B64UUID> {

	@Serial
	private static final long serialVersionUID = 8193871979895082781L;
	
	private static final Encoder encoder = Base64.getUrlEncoder();
	private static final Decoder decoder = Base64.getUrlDecoder();
	
	private final UUID value;
	
	private B64UUID(UUID value) {
		this.value = value;
	}
	
	public B64UUID(long mostSigBits, long leastSigBits) {
		this.value = new UUID(mostSigBits, leastSigBits);
	}
	
	public static B64UUID randomUUID() {
		return new B64UUID(UUID.randomUUID());
	}
	
	public static B64UUID nameUUIDFromBytes(byte[] name) {
		return new B64UUID(UUID.nameUUIDFromBytes(name));
	}
	
	public static B64UUID fromString(String base64) {
		if(base64 == null) {
			throw new IllegalArgumentException("The given base64 string is null");
		}
		if(base64.length() > 22) {
			throw new IllegalArgumentException(String.format("The given base64 UUID %s is too long", base64));
		}
		if(base64.length() < 22) {
			throw new IllegalArgumentException(String.format("The given base64 UUID %s is too short", base64));
		}
		
		ByteBuffer buffer = ByteBuffer.wrap(decoder.decode(base64));
		return new B64UUID(new UUID(buffer.getLong(), buffer.getLong()));
	}
	
	public static B64UUID fromUUID(UUID uuid) {
		if(uuid == null) {
			throw new IllegalArgumentException("The given parameter uuid is null");
		}
		
		return new B64UUID(uuid);
	}
	
	public long getLeastSignificantBits() {
		return this.value.getLeastSignificantBits();
	}
	
	public long getMostSignificantBits() {
		return this.value.getMostSignificantBits();
	}
	
	public int version() {
		return this.value.version();
	}
	
	public int variant() {
		return this.value.variant();
	}
	
	public long timestamp() {
		return this.value.timestamp();
	}
	
	public int clockSequence() {
		return this.value.clockSequence();
	}
	
	public long node() {
		return this.value.node();
	}

    @Override
    public String toString() {
        byte[] src = ByteBuffer.wrap(new byte[16])
                .putLong(value.getMostSignificantBits())
                .putLong(value.getLeastSignificantBits())
                .array();
        
        return encoder.encodeToString(src).substring(0, 22);
    }

	@Override
	public int hashCode() {
		return this.value == null ? 0 : this.value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		B64UUID other = (B64UUID) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(B64UUID o) {
		if(this.equals(o)) {
			return 0;
		} else if(this.value == null) {
			return -1;
		} else if(o.value == null) {
			return 1;
		}
		
		return this.value.compareTo(o.value);
	}
}
