package com.CoreCommerce.domain;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class Pagination {

	private int page;          // 현재 페이지
    private int size;          // 페이지당 개수
    private int totalCount;    // 전체 데이터 수
    private int totalPages;    // 전체 페이지 수
    private int offset;        // DB offset

    public Pagination(int page, int size, int totalCount) {
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;

        this.totalPages = (int) Math.ceil((double) totalCount / size);
        this.offset = (page - 1) * size;
    }

    public boolean isFirst() {
        return page == 1;
    }

    public boolean isLast() {
        return page >= totalPages;
    }
}
