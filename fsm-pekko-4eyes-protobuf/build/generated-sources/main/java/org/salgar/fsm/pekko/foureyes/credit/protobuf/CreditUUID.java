// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: org/salgar/fsm/pekko/foureyes/credit/protobuf/Credit.proto

package org.salgar.fsm.pekko.foureyes.credit.protobuf;

/**
 * Protobuf type {@code org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID}
 */
public final class CreditUUID extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID)
    CreditUUIDOrBuilder {
private static final long serialVersionUID = 0L;
  // Use CreditUUID.newBuilder() to construct.
  private CreditUUID(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private CreditUUID() {
    creditUUID_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new CreditUUID();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private CreditUUID(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            java.lang.String s = input.readStringRequireUtf8();

            creditUUID_ = s;
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.salgar.fsm.pekko.foureyes.credit.protobuf.Credit.internal_static_org_salgar_fsm_pekko_foureyes_credit_protobuf_CreditUUID_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.salgar.fsm.pekko.foureyes.credit.protobuf.Credit.internal_static_org_salgar_fsm_pekko_foureyes_credit_protobuf_CreditUUID_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID.class, org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID.Builder.class);
  }

  public static final int CREDITUUID_FIELD_NUMBER = 1;
  private volatile java.lang.Object creditUUID_;
  /**
   * <code>string creditUUID = 1;</code>
   * @return The creditUUID.
   */
  @java.lang.Override
  public java.lang.String getCreditUUID() {
    java.lang.Object ref = creditUUID_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      creditUUID_ = s;
      return s;
    }
  }
  /**
   * <code>string creditUUID = 1;</code>
   * @return The bytes for creditUUID.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getCreditUUIDBytes() {
    java.lang.Object ref = creditUUID_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      creditUUID_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(creditUUID_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, creditUUID_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(creditUUID_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, creditUUID_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID)) {
      return super.equals(obj);
    }
    org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID other = (org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID) obj;

    if (!getCreditUUID()
        .equals(other.getCreditUUID())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + CREDITUUID_FIELD_NUMBER;
    hash = (53 * hash) + getCreditUUID().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID)
      org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUIDOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.salgar.fsm.pekko.foureyes.credit.protobuf.Credit.internal_static_org_salgar_fsm_pekko_foureyes_credit_protobuf_CreditUUID_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.salgar.fsm.pekko.foureyes.credit.protobuf.Credit.internal_static_org_salgar_fsm_pekko_foureyes_credit_protobuf_CreditUUID_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID.class, org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID.Builder.class);
    }

    // Construct using org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      creditUUID_ = "";

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.salgar.fsm.pekko.foureyes.credit.protobuf.Credit.internal_static_org_salgar_fsm_pekko_foureyes_credit_protobuf_CreditUUID_descriptor;
    }

    @java.lang.Override
    public org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID getDefaultInstanceForType() {
      return org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID.getDefaultInstance();
    }

    @java.lang.Override
    public org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID build() {
      org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID buildPartial() {
      org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID result = new org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID(this);
      result.creditUUID_ = creditUUID_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID) {
        return mergeFrom((org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID other) {
      if (other == org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID.getDefaultInstance()) return this;
      if (!other.getCreditUUID().isEmpty()) {
        creditUUID_ = other.creditUUID_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object creditUUID_ = "";
    /**
     * <code>string creditUUID = 1;</code>
     * @return The creditUUID.
     */
    public java.lang.String getCreditUUID() {
      java.lang.Object ref = creditUUID_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        creditUUID_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string creditUUID = 1;</code>
     * @return The bytes for creditUUID.
     */
    public com.google.protobuf.ByteString
        getCreditUUIDBytes() {
      java.lang.Object ref = creditUUID_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        creditUUID_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string creditUUID = 1;</code>
     * @param value The creditUUID to set.
     * @return This builder for chaining.
     */
    public Builder setCreditUUID(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      creditUUID_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string creditUUID = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearCreditUUID() {
      
      creditUUID_ = getDefaultInstance().getCreditUUID();
      onChanged();
      return this;
    }
    /**
     * <code>string creditUUID = 1;</code>
     * @param value The bytes for creditUUID to set.
     * @return This builder for chaining.
     */
    public Builder setCreditUUIDBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      creditUUID_ = value;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID)
  }

  // @@protoc_insertion_point(class_scope:org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID)
  private static final org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID();
  }

  public static org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<CreditUUID>
      PARSER = new com.google.protobuf.AbstractParser<CreditUUID>() {
    @java.lang.Override
    public CreditUUID parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new CreditUUID(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<CreditUUID> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<CreditUUID> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
