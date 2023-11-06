# base64-uuid

This lib is a simple java.util.UUID wrapper that provide base64 serialization and deserialization.
It provide a B64UUID class that behave exactly the same way as native java's UUID except for toString() method that encode current UUID in base64.

```java
B64UUID uuid = B64UUID.randomUUID();
System.out.println(uuid); // R4tjfEWXQIi7ajHd2bhSYQ
```
