package com.codesquad.airbnb.room;

import com.codesquad.airbnb.charge.ChargeBill;
import com.codesquad.airbnb.charge.ChargeManager;
import com.codesquad.airbnb.common.embeddable.GuestGroup;
import com.codesquad.airbnb.common.embeddable.StayDate;
import com.codesquad.airbnb.room.dto.RoomDetailResponse;
import com.codesquad.airbnb.room.dto.RoomSearCondition;
import com.codesquad.airbnb.room.dto.RoomSearchResponse;
import com.codesquad.airbnb.room.entity.Room;
import com.codesquad.airbnb.room.repository.RoomRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ChargeManager chargeManager;

    public List<RoomSearchResponse> searchRooms(RoomSearCondition condition) {
        List<Room> rooms = roomRepository.searchWithCondition(condition);
        return rooms.stream()
            .map(RoomSearchResponse::from)
            .collect(Collectors.toList());
    }

    public RoomDetailResponse findRoom(Integer id) {
        Room room = roomRepository.findByIdWithDetailAndDistrictAndImages(id)
            .orElseThrow(() -> new IllegalStateException("숙소 정보가 존재하지 않습니다."));

        return RoomDetailResponse.from(room);
    }

    public ChargeBill showCharge(Integer id, StayDate stayDate, GuestGroup guestGroup) {
        Room room = roomRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("숙소 정보가 존재하지 않습니다."));

        return chargeManager.createBill(room, stayDate, guestGroup);
    }

}
