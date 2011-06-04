package org.mybatis.jpetstore.persistence;

import org.mybatis.jpetstore.domain.Sequence;

public interface SequenceMapper {

  Sequence getSequence(Sequence sequence);
  void updateSequence(Sequence sequence);
}
