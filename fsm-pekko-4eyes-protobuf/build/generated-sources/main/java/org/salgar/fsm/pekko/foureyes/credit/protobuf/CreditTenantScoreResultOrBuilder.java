// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: org/salgar/fsm/pekko/foureyes/credit/protobuf/Credit.proto

package org.salgar.fsm.pekko.foureyes.credit.protobuf;

public interface CreditTenantScoreResultOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditTenantScoreResult)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string personalId = 1;</code>
   * @return The personalId.
   */
  java.lang.String getPersonalId();
  /**
   * <code>string personalId = 1;</code>
   * @return The bytes for personalId.
   */
  com.google.protobuf.ByteString
      getPersonalIdBytes();

  /**
   * <code>double creditScore = 2;</code>
   * @return The creditScore.
   */
  double getCreditScore();
}