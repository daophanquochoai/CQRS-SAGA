
# CQRS và Saga trong Kiến trúc Phần mềm

## Mục lục
1. [Giới thiệu về CQRS](#giới-thiệu-về-cqrs)
2. [Giới thiệu về Saga](#giới-thiệu-về-saga)
3. [Lợi ích của CQRS và Saga](#lợi-ích-của-cqrs-và-saga)
4. [Cách triển khai CQRS và Saga](#cách-triển-khai-cqrs-và-saga)
5. [Kết luận](#kết-luận)

---

## Giới thiệu về CQRS

**CQRS** (Command Query Responsibility Segregation - Phân tách trách nhiệm Command và Query) là một mẫu thiết kế trong đó các thao tác ghi (Command) và đọc (Query) dữ liệu được tách biệt thành hai phần độc lập. Điều này giúp hệ thống dễ dàng mở rộng, tăng hiệu năng và đảm bảo tính nhất quán dữ liệu theo từng mục tiêu khác nhau.

### Đặc điểm chính của CQRS:
- **Command**: Được sử dụng để thực hiện các hành động làm thay đổi trạng thái của hệ thống, như tạo mới, cập nhật, hoặc xóa dữ liệu.
- **Query**: Được sử dụng để truy vấn dữ liệu, đảm bảo rằng không có thay đổi nào đối với trạng thái hệ thống khi thực hiện truy vấn.

### Khi nào sử dụng CQRS:
- Hệ thống có nhu cầu mở rộng quy mô (scalability) cao.
- Hệ thống cần hỗ trợ nhiều hành vi đọc và ghi khác nhau.
- Khi muốn tách riêng các thao tác đọc và ghi để tối ưu hóa hiệu suất.

---

## Giới thiệu về Saga

**Saga** là một mẫu thiết kế dùng để quản lý giao dịch phân tán trong hệ thống phân tán. Trong một hệ thống có nhiều microservice, mỗi dịch vụ có thể thực hiện các tác vụ khác nhau nhưng vẫn cần phải đảm bảo tính toàn vẹn của dữ liệu xuyên suốt quá trình. Saga giải quyết vấn đề này thông qua việc thực hiện từng bước của giao dịch trong các dịch vụ riêng lẻ, và nếu một bước thất bại, nó sẽ kích hoạt các hành động bù trừ (compensating actions) để hủy bỏ các bước trước đó.

### Có hai loại Saga chính:
- **Choreography**: Các dịch vụ tự quản lý và điều phối Saga thông qua việc gửi các sự kiện (event-driven).
- **Orchestration**: Một dịch vụ trung tâm (Saga Orchestrator) quản lý và điều phối toàn bộ luồng giao dịch.

### Lợi ích của Saga:
- Đảm bảo tính nhất quán dữ liệu trong các hệ thống phân tán.
- Giảm thiểu độ phức tạp khi triển khai các giao dịch phân tán mà không cần sử dụng các giao dịch ACID truyền thống.

---

## Lợi ích của CQRS và Saga

### CQRS
- **Mở rộng hiệu năng**: Giúp hệ thống dễ dàng mở rộng (scale) theo chiều dọc hoặc chiều ngang.
- **Tách biệt trách nhiệm**: Command và Query được tách biệt, cho phép tối ưu hóa độc lập từng phần.
- **Tối ưu hóa dữ liệu**: Cho phép tối ưu hóa các mô hình dữ liệu khác nhau cho thao tác ghi và đọc.

### Saga
- **Xử lý giao dịch phân tán**: Cho phép quản lý các giao dịch phức tạp trong hệ thống microservices.
- **Khả năng hồi phục**: Nếu một bước của giao dịch thất bại, Saga có khả năng thực hiện các hành động bù trừ để quay trở lại trạng thái ban đầu.

---

## Cách triển khai CQRS và Saga

### Triển khai CQRS
1. **Tách mô hình dữ liệu**: Mô hình dữ liệu cho Command (ghi) và Query (đọc) được tách riêng.
2. **Sử dụng các lớp dịch vụ**: Command sẽ được xử lý bởi các lớp dịch vụ viết và Query bởi các lớp dịch vụ đọc.
3. **Event Sourcing**: CQRS thường đi kèm với Event Sourcing, nơi các thay đổi trạng thái hệ thống được lưu trữ dưới dạng các sự kiện.

### Triển khai Saga
1. **Orchestration Saga**: Triển khai một dịch vụ trung tâm để điều phối các bước trong giao dịch phân tán.
2. **Choreography Saga**: Sử dụng các sự kiện để các dịch vụ giao tiếp với nhau mà không cần một trung tâm điều phối.
3. **Compensation**: Mỗi bước trong Saga cần phải có khả năng bù trừ nếu xảy ra lỗi trong quá trình thực hiện.

---

## Kết luận

CQRS và Saga là hai mẫu thiết kế quan trọng trong việc xây dựng các hệ thống phân tán và microservices. **CQRS** giúp tối ưu hóa hiệu suất và mở rộng hệ thống thông qua việc tách biệt logic ghi và đọc dữ liệu. **Saga** đảm bảo tính nhất quán và khả năng phục hồi của các giao dịch phân tán. Sự kết hợp của CQRS và Saga giúp xây dựng hệ thống phân tán mạnh mẽ, đảm bảo tính toàn vẹn dữ liệu và tăng cường khả năng mở rộng.

---

**Tham khảo thêm:**
- [CQRS Documentation](https://martinfowler.com/bliki/CQRS.html)
- [Saga Pattern](https://microservices.io/patterns/data/saga.html)

---
