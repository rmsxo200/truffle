package com.toy.truffle.destination.repository;

import com.toy.truffle.destination.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination, String> {
    //destinationCd List로 destination 조회
    List<Destination> findByDestinationCdIn(List<String> destinationCdList);

    //destinationCd like 조회
    List<Destination> findByDestinationCdStartingWith (String startDestinationCd);
}
