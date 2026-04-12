## 1. Hai cách để trigger (kích hoạt) GitHub Copilot

Có hai cách phổ biến để kích hoạt gợi ý từ GitHub Copilot:

- **Gõ code theo ngữ cảnh**: Khi bạn viết code (ví dụ: tên hàm, comment, hoặc logic dở dang), Copilot sẽ tự động đưa ra gợi ý ngay trong editor.
- **Sử dụng phím tắt hoặc lệnh**: Ví dụ nhấn `Tab` để chấp nhận gợi ý, hoặc `Ctrl + Enter` (tuỳ IDE) để mở danh sách nhiều gợi ý khác nhau.

### Sự khác nhau giữa Inline Suggestion và Copilot Chat/Panel

- **Inline Suggestion**:

  - Hiển thị trực tiếp trong editor khi bạn đang gõ code.
  - Đưa ra gợi ý nhanh, theo thời gian thực.
  - Phù hợp với các đoạn code nhỏ, theo ngữ cảnh hiện tại.
- **Copilot Chat/Panel**:

  - Là một giao diện riêng để bạn đặt câu hỏi hoặc yêu cầu sinh code.
  - Hỗ trợ các yêu cầu phức tạp, giải thích, hoặc logic nhiều bước.
  - Phù hợp để tạo các đoạn code lớn hoặc tìm hiểu kiến thức.

---

## 2. Nguyên tắc của một prompt "tốt"

Dựa trên trải nghiệm, một prompt tốt nên tuân theo các nguyên tắc sau:

1. **Rõ ràng và cụ thể**

   - Mô tả chính xác bạn muốn gì (ngôn ngữ, framework, hành vi mong muốn).
   - Tránh các yêu cầu mơ hồ như “viết code login”.
2. **Cung cấp ngữ cảnh**

   - Bao gồm các thông tin liên quan như code hiện tại, input/output, hoặc ràng buộc.
   - Ví dụ: nêu rõ đang dùng React, Spring Boot, hoặc database nào.
3. **Chia nhỏ bài toán**

   - Không yêu cầu quá nhiều thứ trong một lần.
   - Tách thành các bước nhỏ giúp Copilot tạo code chính xác và dễ maintain hơn.

---

## 3. Vấn đề gặp phải với code do Copilot sinh ra

Có, đã gặp trường hợp Copilot sinh ra code sai hoặc chưa tối ưu.

### Ví dụ:

- Copilot tạo câu query database mà không validate input, dẫn đến nguy cơ **SQL Injection**.

### Cách phát hiện:

- Kiểm tra code và thấy input từ người dùng được nối trực tiếp vào chuỗi SQL.
- Đây là lỗi bảo mật phổ biến trong backend.

### Cách khắc phục:

- Thay thế bằng **prepared statement / parameterized query**.
- Với Spring Boot, sử dụng `JpaRepository` hoặc `@Query` có tham số thay vì nối chuỗi thủ công.

### Bài học rút ra:

- Luôn review lại code do Copilot sinh ra.
- Không nên mặc định code đó đã đúng hoặc an toàn.
