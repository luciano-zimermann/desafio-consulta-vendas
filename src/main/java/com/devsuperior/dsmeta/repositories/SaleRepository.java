package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.projections.SaleSellerProjection;
import com.devsuperior.dsmeta.projections.SellerAmountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long>
{
    String SEARCH_SALES_REPORT_QUERY = "SELECT tb_sales.id, tb_sales.date, tb_sales.amount, tb_seller.name as sellerName " +
                                       "FROM tb_sales " +
                                       "INNER JOIN tb_seller ON tb_sales.seller_id = tb_seller.id " +
                                       "WHERE tb_sales.date BETWEEN :minDate AND :maxDate " +
                                       "AND UPPER(tb_seller.name) LIKE CONCAT('%', UPPER(:name), '%') " +
                                       "ORDER BY tb_sales.date DESC";

    String COUNT_SALES_REPORT_QUERY =  "SELECT COUNT(*) " +
                                       "FROM tb_sales " +
                                       "INNER JOIN tb_seller ON tb_sales.seller_id = tb_seller.id " +
                                       "WHERE tb_sales.date BETWEEN :minDate AND :maxDate " +
                                       "AND UPPER(tb_seller.name) LIKE CONCAT('%', UPPER(:name), '%')";

    String SEARCH_BY_SELLER_QUERY = "SELECT tb_seller.name, SUM(tb_sales.amount) AS amount " +
                                    "FROM tb_seller " +
                                    "INNER JOIN tb_sales on tb_seller.id = tb_sales.seller_id " +
                                    "WHERE tb_sales.date BETWEEN :minDate AND :maxDate " +
                                    "GROUP BY tb_seller.name;";

    @Query( nativeQuery = true, value = SEARCH_SALES_REPORT_QUERY, countQuery = COUNT_SALES_REPORT_QUERY )
    Page<SaleSellerProjection> searchSalesReport( LocalDate minDate, LocalDate maxDate, String name, Pageable pageable );

    @Query( nativeQuery = true, value = SEARCH_BY_SELLER_QUERY )
    List<SellerAmountProjection> searchBySeller( LocalDate minDate, LocalDate maxDate );
}
