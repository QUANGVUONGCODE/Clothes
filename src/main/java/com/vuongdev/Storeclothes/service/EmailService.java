package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.entity.Invoice;
import com.vuongdev.Storeclothes.entity.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPaymentSuccessEmail(Order order, Invoice invoice) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(order.getEmail());
            helper.setSubject("Hóa đơn đơn hàng " + order.getOrderCode());

            String totalMoney = formatMoney(order.getTotalMoney());

            String html = """
                    <div style="font-family: Arial, sans-serif; line-height: 1.6;">
                        <h2>Thanh toán thành công</h2>
                        <p>Xin chào <b>%s</b>,</p>
                        <p>Cảm ơn bạn đã mua hàng tại <b>Shop Clothes</b>.</p>

                        <h3>Thông tin hóa đơn</h3>
                        <p><b>Mã đơn hàng:</b> %s</p>
                        <p><b>Mã hóa đơn:</b> %s</p>
                        <p><b>Số điện thoại:</b> %s</p>
                        <p><b>Địa chỉ nhận hàng:</b> %s</p>
                        <p><b>Tổng tiền:</b> %s</p>
                        <p><b>Trạng thái thanh toán:</b> %s</p>

                        <br>
                        <p>Shop sẽ xử lý và giao đơn hàng cho bạn trong thời gian sớm nhất.</p>
                        <p>Trân trọng,<br><b>Shop Clothes</b></p>
                    </div>
                    """.formatted(
                    order.getFullName(),
                    order.getOrderCode(),
                    invoice.getInvoiceCode(),
                    order.getPhoneNumber(),
                    order.getAddress(),
                    totalMoney,
                    invoice.getPaymentStatus()
            );

            helper.setText(html, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Gửi email hóa đơn thất bại", e);
        }
    }

    public void sendCodOrderConfirmationEmail(Order order) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(order.getEmail());
            helper.setSubject("Xác nhận đơn hàng " + order.getOrderCode());

            String totalMoney = formatMoney(order.getTotalMoney());

            String html = """
                    <div style="font-family: Arial, sans-serif; line-height: 1.6;">
                        <h2>Đặt hàng thành công</h2>
                        <p>Xin chào <b>%s</b>,</p>
                        <p>Đơn hàng của bạn đã được ghi nhận.</p>

                        <p><b>Mã đơn hàng:</b> %s</p>
                        <p><b>Tổng tiền:</b> %s</p>
                        <p><b>Phương thức thanh toán:</b> Thanh toán khi nhận hàng</p>
                        <p><b>Trạng thái:</b> Chưa thanh toán</p>

                        <br>
                        <p>Shop sẽ liên hệ và giao hàng cho bạn sớm nhất.</p>
                    </div>
                    """.formatted(
                    order.getFullName(),
                    order.getOrderCode(),
                    totalMoney
            );

            helper.setText(html, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Gửi email xác nhận COD thất bại", e);
        }
    }

    private String formatMoney(java.math.BigDecimal money) {
        if (money == null) {
            return "0 VNĐ";
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(money);
    }

}
