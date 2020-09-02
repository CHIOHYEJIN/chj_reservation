package yogaClassReservation;

import org.springframework.beans.BeanUtils;
import yogaClassReservation.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    ReservationRepository reservationRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOutOfClass_RequestReservationCancel(@Payload OutOfClass outOfClass){

        if(outOfClass.isMe()){
            System.out.println("##### listener RequestReservationCancel : " + outOfClass.toJson());

            List<Reservation> reservationList = reservationRepository.findByReserveId(outOfClass.getReserveId());

            // 강좌 수강 정원이 찼을 경우 (outOfClass)일 경우, 예약 취소 이벤트 publish
            // OutOfClass Event 수신 시, ReserveCanceled 이벤트 발행
            for(Reservation reservation : reservationList)
            {
                ReserveCanceled reserveCanceled = new ReserveCanceled();
                BeanUtils.copyProperties(this, reserveCanceled);

                reserveCanceled.setId(reservation.getId());
                reserveCanceled.setReserveId(reservation.getReserveId());
                reserveCanceled.setClassId(reservation.getClassId());
                reserveCanceled.setGuestAddress(reservation.getGuestAddress());
                reserveCanceled.setUserId(reservation.getUserId());
                reserveCanceled.setPrice(reservation.getPrice());
                reserveCanceled.setReservationStatus("ReserveCanceled");

                reserveCanceled.publish();
            }
        }
    }

}
