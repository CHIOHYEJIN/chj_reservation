
package yogaClassReservation;

public class OutOfClass extends AbstractEvent {

    private Long id;
    private Long reserveId;
    private Long customerId;
    private Long classStock;

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
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public Long getClassStock() {
        return classStock;
    }

    public void setClassStock(Long classStock) {
        this.classStock = classStock;
    }
}
