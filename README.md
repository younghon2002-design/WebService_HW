웹서비스설계 실습1

수업 예제 코드를 바탕으로 과제 지시사항에 따라 수정하여 실습을 수행하였습니다.

바꾼 내용

API 추가 (5개 → 8개)
POST /api/v1/items/{id}/likes 좋아요 추가
PUT /api/v1/items/{id}/stock 재고 수정 (점검 중 가정, 503)
DELETE /api/v1/items 전체 삭제 (서버 오류 가정, 500)

응답 포맷 통일: 모든 응답을 { "status": "success" | "error", "data": ... }로 감싸도록 success(), fail() 메서드 추가

상태 코드
상품 등록을 200 → 201로 변경
이름이 없거나 가격이 없거나 음수면 400
삭제를 204 → 200으로 변경 (응답 본문에 status를 담기 위해)
404·500·503도 오류 메시지를 본문에 담아 반환
ItemDto에 likes 필드 추가

미들웨어: 예제의 LoggingInterceptor, WebConfig를 가져와 요청 시작·완료(상태 코드)를 콘솔에 출력

테스트 결과 캡처는 screenshots 폴더에 있습니다.
