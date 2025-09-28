package com.congresoSpring.control.participants;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository  extends JpaRepository<Participant,Long> {
//    Optional<Participant> findByParticipantId(Long participantId);
//    Participant earnedPoints(Long participantId);
//    Participant findIfCertificateReached(Integer certificateReachedPoints);
 //   Optional<Participant> findBySpecialEventAccessPoints(Integer points);

}
