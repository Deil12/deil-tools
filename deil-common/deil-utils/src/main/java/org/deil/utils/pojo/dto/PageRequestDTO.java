package org.deil.utils.pojo.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public class PageRequestDTO {
    @Range(min = 1L)
    @NotNull
    private Integer page;

    @Range(min = 1L, max = 100L)
    @NotNull
    private Integer size;

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PageRequestDTO))
            return false;
        PageRequestDTO other = (PageRequestDTO) o;
        if (!other.canEqual(this))
            return false;
        Object this$page = getPage(), other$page = other.getPage();
        if ((this$page == null) ? (other$page != null) : !this$page.equals(other$page))
            return false;
        Object this$size = getSize(), other$size = other.getSize();
        return !((this$size == null) ? (other$size != null) : !this$size.equals(other$size));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PageRequestDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $page = getPage();
        result = result * 59 + (($page == null) ? 43 : $page.hashCode());
        Object $size = getSize();
        return result * 59 + (($size == null) ? 43 : $size.hashCode());
    }

    public String toString() {
        return "PageRequestDTO(page=" + getPage() + ", size=" + getSize() + ")";
    }

    protected PageRequestDTO(PageRequestDTOBuilder<?, ?> b) {
        this.page = b.page;
        this.size = b.size;
    }

    public static PageRequestDTOBuilder<?, ?> builder() {
        return new PageRequestDTOBuilderImpl();
    }

    private static final class PageRequestDTOBuilderImpl extends PageRequestDTOBuilder<PageRequestDTO, PageRequestDTOBuilderImpl> {
        private PageRequestDTOBuilderImpl() {
        }

        protected PageRequestDTOBuilderImpl self() {
            return this;
        }

        public PageRequestDTO build() {
            return new PageRequestDTO(this);
        }
    }

    public static abstract class PageRequestDTOBuilder<C extends PageRequestDTO, B extends PageRequestDTOBuilder<C, B>> {
        private Integer page;

        private Integer size;

        protected abstract B self();

        public abstract C build();

        public B page(Integer page) {
            this.page = page;
            return self();
        }

        public B size(Integer size) {
            this.size = size;
            return self();
        }

        public String toString() {
            return "PageRequestDTO.PageRequestDTOBuilder(page=" + this.page + ", size=" + this.size + ")";
        }
    }

    public Integer getPage() {
        return this.page;
    }

    public Integer getSize() {
        return this.size;
    }
}