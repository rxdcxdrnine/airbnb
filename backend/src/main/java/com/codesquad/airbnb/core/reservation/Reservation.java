package com.codesquad.airbnb.core.reservation;

import com.codesquad.airbnb.core.common.embeddable.GuestGroup;
import com.codesquad.airbnb.core.common.embeddable.StayDate;
import com.codesquad.airbnb.core.common.embeddable.StayTime;
import com.codesquad.airbnb.core.member.Member;
import com.codesquad.airbnb.core.room.entity.Room;
import com.codesquad.airbnb.exception.ErrorCode;
import com.codesquad.airbnb.exception.unchecked.NotAvailableException;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    public enum ReservationState {BOOKED, CANCELED, COMPLETED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer id;

    private Integer totalPrice;

    @Embedded
    private GuestGroup guestGroup;

    @Embedded
    private StayDate stayDate;

    @Embedded
    private StayTime stayTime;

    @Enumerated(value = EnumType.STRING)
    private ReservationState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private Member guest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public Reservation(Member guest, Room room, Integer totalPrice, GuestGroup guestGroup,
        StayDate stayDate, StayTime stayTime, ReservationState state) {
        this.guest = guest;
        this.room = room;
        this.totalPrice = totalPrice;
        this.guestGroup = guestGroup;
        this.stayDate = stayDate;
        this.stayTime = stayTime;
        this.state = state;
    }

    public void cancel() {
        if (this.state != ReservationState.BOOKED) {
            throw new NotAvailableException(ErrorCode.RESERVATION_NOT_CANCELED);
        }

        this.state = ReservationState.CANCELED;
    }
}
