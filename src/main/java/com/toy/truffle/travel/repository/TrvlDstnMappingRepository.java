package com.toy.truffle.travel.repository;

import com.toy.truffle.travel.entity.TrvlDstnMapping;
import com.toy.truffle.travel.entity.TrvlDstnMappingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrvlDstnMappingRepository extends JpaRepository<TrvlDstnMapping, TrvlDstnMappingId> {
}
