package yogaClassReservation;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Reservation_table")
public class Reservation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long reserveId;
    private Long userId;
    private Long price;
    private String reservationStatus;
    private String guestAddress;
    private Long classId;

    @PostPersist
    public void onPostPersist(){
        yogaClassReservation.external.Payment payment = new yogaClassReservation.external.Payment();
        // mappings goes here
        payment.setId(getId());
        payment.setReserveId(getReserveId());
        payment.setUserId(getUserId());
        payment.setGuestAddress(getGuestAddress());
        payment.setPrice(this.getPrice());
        payment.setPaymentStatus("PaymentSucceed");

        ReservationApplication.applicationContext.getBean(yogaClassReservation.external.PaymentService.class)
                .paymentRequest(payment);

        // 결제까지 완료되면 최종적으로 예약 완료 이벤트 발생
        Reserved reserved = new Reserved();
        BeanUtils.copyProperties(this, reserved);
        reserved.setReservationStatus("Reserved");

        reserved.publishAfterCommit();
    }

    @PreRemove
    public void onPreRemove(){
        ReserveCanceled reserveCanceled = new ReserveCanceled();
        BeanUtils.copyProperties(this, reserveCanceled);
        reserveCanceled.setId(getId());
        reserveCanceled.setReservationStatus("ReserveCanceled");
        reserveCanceled.publishAfterCommit();

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getReserveId() {
        return reserveId;
    }

    public void setReserveId(Long reserveId) {
        this.reserveId = reserveId;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
    public String getGuestAddress() {
        return guestAddress;
    }

    public void setGuestAddress(String guestAddress) {
        this.guestAddress = guestAddress;
    }
    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }




}
