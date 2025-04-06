package dev.luismachadoreis.flighttracker.server.application;

import dev.luismachadoreis.flighttracker.server.application.dto.PingDTO;
import dev.luismachadoreis.flighttracker.server.domain.Ping;
import dev.luismachadoreis.flighttracker.server.domain.PingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PingService {
    
    private final PingRepository pingRepository;
    
    public PingDTO createPing(PingDTO pingDTO) {
        var ping = new Ping(
            pingDTO.icao24(),
            pingDTO.callsign(),
            pingDTO.originCountry(),
            pingDTO.timePosition(),
            pingDTO.lastContact(),
            pingDTO.longitude(),
            pingDTO.latitude(),
            pingDTO.baroAltitude(),
            pingDTO.onGround(),
            pingDTO.velocity(),
            pingDTO.trueTrack(),
            pingDTO.verticalRate(),
            pingDTO.sensors(),
            pingDTO.geoAltitude(),
            pingDTO.squawk(),
            pingDTO.spi(),
            pingDTO.positionSource()
        );
        
        ping.registerPingCreated();
        pingRepository.save(ping);
        
        return pingDTO;
    }

    @Transactional(readOnly = true)
    public List<PingDTO> getPings(final Integer limit) {
        return pingRepository
            .findTopNPings(limit)
                .stream()
                    .map(ping -> new PingDTO(
                        ping.getIcao24(),
                        ping.getCallsign(), 
                        ping.getOriginCountry(),
                        ping.getTimePosition(),
                        ping.getLastContact(),
                        ping.getLongitude(),
                        ping.getLatitude(),
                        ping.getBaroAltitude(),
                        ping.getOnGround(),
                        ping.getVelocity(),
                        ping.getTrueTrack(),
                        ping.getVerticalRate(),
                        ping.getSensors(),
                        ping.getGeoAltitude(),
                        ping.getSquawk(),
                        ping.getSpi(),
                        ping.getPositionSource()
                    ))
            .toList();
    }
} 