package database.models.payment;

import database.connection.HibernateUtil;
import database.models.Model;
import database.models.modality.Modality;
import database.models.modality.Student;
import database.models.user.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "payments")
public class Payment extends Model<Payment> {

    @Column(name = "referenceMonth")
    private LocalDate reference_month;

    @ManyToOne
    @JoinColumn(name = "student_id", foreignKey = @ForeignKey(name = "payment_student_id"))
    private Student student;

    @Column(name = "payday")
    private LocalDate payday;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "amountPaid")
    private BigDecimal amount_paid;

    public static BigDecimal totalValueModalities(Student student){
        List<Modality> modalities = student.getModalities();
        BigDecimal valueAmount = BigDecimal.ZERO;

        for (Modality modality: modalities) {
            valueAmount = valueAmount.add(modality.getValue());
        }

        return valueAmount;
    }

    public static List<Payment> paymentStatusOpen(Integer codStudent){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Payment> payment;

        Query query = session.createQuery("SELECT p FROM database.models.payment.Payment as p " +
                                            "inner join p.student as student "+
                                            "WHERE p.amount > p.amount_paid and (student.id =:codStudent or 0 =:codStudent) ");
        query.setParameter("codStudent", codStudent);
        payment = query.getResultList();
        session.close();

        return payment;
    }

    public static List<Payment> paymentStatusPaid(Integer codStudent){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Payment> payment;

        Query query = session.createQuery("SELECT p FROM database.models.payment.Payment as p " +
                                            "inner join p.student as student "+
                                            "WHERE p.amount = p.amount_paid and (student.id =:codStudent or 0 =:codStudent) ");
        query.setParameter("codStudent", codStudent);
        payment = query.getResultList();
        session.close();

        return payment;
    }

    public static List<Payment> findPaymentsByStudent(Student student){
        if (student == null) {
            return null;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Payment> payment;

        Query query = session.createQuery("SELECT p FROM database.models.payment.Payment as p " +
                "inner join p.student as student "+
                "WHERE (student.id =:codStudent or 0 =:codStudent) ");
        query.setParameter("codStudent", student.getId());
        payment = query.getResultList();
        session.close();

        return payment;
    }

    public LocalDate getReference_month() {
        return reference_month;
    }

    public void setReference_month(LocalDate reference_month) {
        this.reference_month = reference_month;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LocalDate getPayday() {
        return payday;
    }

    public void setPayday(LocalDate payday) {
        this.payday = payday;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(BigDecimal amount_paid) {
        this.amount_paid = amount_paid;
    }

    @Override
    public String[] getResult() {
        return new String[0];
    }

    @Override
    public List<Payment> filter(String value) {
        return null;
    }
}
