package com.walletguardians.walletguardiansapi.domain.expenses.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.walletguardians.walletguardiansapi.domain.expenses.service.dto.OcrResponse.ImageResult.BoundingPoly.Vertex;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OcrResponse {
    private String version; // OCR API 버전 정보 (V2 사용)
    private String requestId; // API 요청 고유 ID
    private long timestamp; // API 호출 타임스탬프
    private List<ImageResult> images; // OCR 결과 이미지 리스트

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageResult {
        private String uid; // 영수증 이미지의 UID
        private String name; // 이미지 파일명
        private String inferResult; // OCR 분석 결과 (SUCCESS, FAILURE, ERROR)
        private String message; // 결과 메시지
        private ValidationResult validationResult; // 유효성 검사 결과
        private ConvertedImageInfo convertedImageInfo; // 변환된 이미지 정보
        private Receipt receipt; // 영수증 분석 결과

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ValidationResult {
            private String result; // 유효성 검사 상태
            private String message; // 유효성 검사 메시지
        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ConvertedImageInfo {
            private int width; // 변환된 이미지 가로 길이
            private int height; // 변환된 이미지 세로 길이
            private int pageIndex; // 페이지 인덱스
        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Receipt {
            private ReceiptMeta meta; // 영수증 메타 정보
            private ReceiptResult result; // 영수증 OCR 결과

            @Getter
            @Setter
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ReceiptMeta {
                private String estimatedLanguage; // OCR에서 추정한 언어 (예: ko, en, ja)
            }

            @Getter
            @Setter
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ReceiptResult {
                private StoreInfo storeInfo; // 점포 정보
                private PaymentInfo paymentInfo; // 결제 정보
                private List<SubResult> subResults; // 상품 리스트
                private TotalPrice totalPrice; // 총 결제 금액
                private List<SubTotal> subTotal; // 세부 가격 정보

                @Getter
                @Setter
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class StoreInfo {
                    private CommonField name; // 점포명
                    private CommonField subName; // 지점명
                    private CommonField bizNum; // 사업자등록번호
                    private CommonField movieName; // 영화표 정보
                    private List<CommonField> addresses; // 주소 리스트
                    private List<CommonField> tel; // 전화번호 리스트
                }

                @Getter
                @Setter
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class PaymentInfo {
                    private DateField date; // 결제 날짜
                    private TimeField time; // 결제 시간
                    private CardInfo cardInfo; // 카드 정보
                    private CommonField confirmNum; // 승인 번호

                    @Getter
                    @Setter
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class CardInfo {
                        private CommonField company; // 카드사 정보
                        private CommonField number; // 카드 번호
                    }
                }

                @Getter
                @Setter
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class SubResult {
                    private List<Item> items; // 상품 리스트

                    @Getter
                    @Setter
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class Item {
                        private CommonField name; // 상품명
                        private CommonField code; // 상품 코드
                        private CommonField count; // 수량
                        private Price price; // 가격 정보

                        @Getter
                        @Setter
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class Price {
                            private CommonField price; // 가격
                            private CommonField unitPrice; // 단가
                        }
                    }
                }

                @Getter
                @Setter
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class TotalPrice {
                    private CommonField price; // 총 결제 금액
                }

                @Getter
                @Setter
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class SubTotal {
                    private List<CommonField> taxPrice; // 세금 정보
                    private List<CommonField> discountPrice; // 할인 정보
                }
            }
        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CommonField {
            private String text; // 인식된 텍스트
            private Formatted formatted; // 포맷팅된 값
            private String keyText; // 키 텍스트
            private float confidenceScore; // 신뢰도 (0~1)
            private List<BoundingPoly> boundingPolys; // 영역 정보
            private List<MaskingPoly> maskingPolys; // 마스킹 정보

            @Getter
            @Setter
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Formatted {
                private String value; // 포맷팅된 값
            }
        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class BoundingPoly {
            private List<Vertex> vertices; // 꼭짓점 좌표

            @Getter
            @Setter
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Vertex {
                private double x; // x 좌표
                private double y; // y 좌표
            }
        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class MaskingPoly {
            private List<Vertex> vertices; // 마스킹 좌표
        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DateField {
            private String text; // 날짜 텍스트
            private FormattedDate formatted; // 포맷팅된 날짜
            private String keyText; // 키 텍스트
            private float confidenceScore; // 신뢰도

            @Getter
            @Setter
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class FormattedDate {
                private String year;
                private String month;
                private String day;
            }
        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class TimeField {
            private String text; // 시간 텍스트
            private FormattedTime formatted; // 포맷팅된 시간
            private String keyText; // 키 텍스트
            private float confidenceScore; // 신뢰도

            @Getter
            @Setter
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class FormattedTime {
                private String hour;
                private String minute;
                private String second;
            }
        }
    }
}
