package org.bellamy.base64_uuid;

import java.io.Serial;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.UUID;

/**
 * This class is a simple java.util.UUID wrapper that provide base64 serialization and deserialization.
 * 
 * @see java.util.UUID
 * @author Matthieu Bellamy
 *
 */
public final class B64UUID implements Serializable, Comparable<B64UUID> {

    /**
     * Explicit serialVersionUID for interoperability.
     */
	@Serial
	private static final long serialVersionUID = 8193871979895082781L;
	
	/**
	 * Used to serialize value in base64 compatible with only URL safe characters.
	 */
	private static final Encoder base64UrlEncoder = Base64.getUrlEncoder();
	/**
	 * Used to deserialize base64 value that contain only URL safe characters.
	 */
	private static final Decoder base64UrlDecoder = Base64.getUrlDecoder();
	
	/**
	 * Used to serialize value in base64.
	 */
	private static final Encoder base64Encoder = Base64.getEncoder();
	/**
	 * Used to deserialize base64 value.
	 */
	private static final Decoder base64Decoder = Base64.getDecoder();
	
	/**
	 * The source UUID.
	 */
	private final UUID value;
	
	/**
	 * Constructs a new {@code B64UUID} from the given java.util.UUID 
	 * {@code value}.
	 * 
	 * @param value the UUID value.
	 */
	private B64UUID(UUID value) {
		this.value = value;
	}
	
	/**
	 * @see java.util.UUID#UUID(long, long) 
	 */
	public B64UUID(long mostSigBits, long leastSigBits) {
		this.value = new UUID(mostSigBits, leastSigBits);
	}
	
	/**
	 * @see java.util.UUID#UUID(long, long) 
	 */
	public static B64UUID randomUUID() {
		return new B64UUID(UUID.randomUUID());
	}
	
	/**
	 * @see java.util.UUID#nameUUIDFromBytes(byte[]) 
	 */
	public static B64UUID nameUUIDFromBytes(byte[] name) {
		return new B64UUID(UUID.nameUUIDFromBytes(name));
	}
	
	/**
	 * Constructs a new {@code B64UUID} from a base64 string.
	 * 
	 * @param base64 the string base64 encoded. It can contains '-' and '_'
	 * that will be taken as base64 characters '/' or '+'.
	 * 
	 * @return the new {@code B64UUID} created.
	 */
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
		
		ByteBuffer buffer = null;
		
		if(base64.contains("/") || base64.contains("+")) {
			buffer = ByteBuffer.wrap(base64Decoder.decode(base64));
		} else {
			buffer = ByteBuffer.wrap(base64UrlDecoder.decode(base64));
		}
		
		return new B64UUID(new UUID(buffer.getLong(), buffer.getLong()));
	}
	
	/**
	 * Constructs a new {@code B64UUID} from an already existing {@code UUID}.
	 * 
	 * @param uuid the source {@code UUID}
	 * 
	 * @return the new {@code B64UUID} created.
	 */
	public static B64UUID fromUUID(UUID uuid) {
		if(uuid == null) {
			throw new IllegalArgumentException("The given parameter uuid is null");
		}
		
		return new B64UUID(uuid);
	}
	
	/**
	 * @see java.util.UUID#getLeastSignificantBits()
	 */
	public long getLeastSignificantBits() {
		return this.value.getLeastSignificantBits();
	}
	
	/**
	 * @see java.util.UUID#getMostSignificantBits() 
	 */
	public long getMostSignificantBits() {
		return this.value.getMostSignificantBits();
	}
	
	/**
	 * @see java.util.UUID#version() 
	 */
	public int version() {
		return this.value.version();
	}
	
	/**
	 * @see java.util.UUID#variant() 
	 */
	public int variant() {
		return this.value.variant();
	}
	
	/**
	 * @see java.util.UUID#timestamp() 
	 */
	public long timestamp() {
		return this.value.timestamp();
	}
	
	/**
	 * @see java.util.UUID#clockSequence() 
	 */
	public int clockSequence() {
		return this.value.clockSequence();
	}
	
	/**
	 * @see java.util.UUID#node() 
	 */
	public long node() {
		return this.value.node();
	}
	
	/**
	 * Generate a base64 representation of this {@code B64UUID} as string.
	 */
	public String value() {
		return base64Encoder.encodeToString(getBytes()).substring(0, 22);
	}
	
	/**
	 * Generate a base64 representation of this {@code B64UUID} as string. The
	 * built string will not contains '/' or '+' but respectively '-' or '_' 
	 * instead. The purpose is allow putting returned string in URL without any
	 * other manipulation.
	 */
    @Override
    public String toString() {
        return base64UrlEncoder.encodeToString(getBytes()).substring(0, 22);
    }

    /**
     * @see java.util.UUID#hashCode() 
     */
	@Override
	public int hashCode() {
		return this.value == null ? 0 : this.value.hashCode();
	}

	/**
	 * @see java.util.UUID#equals(Object) 
	 */
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
	
	/**
	 * @see java.util.UUID#compareTo(UUID)
	 */
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
	
	/**
	 * Transform the current UUID value to byte array.
	 * @return the current UUID as byte array.
	 */
	private byte[] getBytes() {
		return ByteBuffer.wrap(new byte[16])
                .putLong(value.getMostSignificantBits())
                .putLong(value.getLeastSignificantBits())
                .array();
	}
}
