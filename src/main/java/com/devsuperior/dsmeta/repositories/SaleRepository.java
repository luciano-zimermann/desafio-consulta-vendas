package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.projections.SellerAmountProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long>
{
    String SEARCH_BY_SELLER_QUERY = "SELECT tb_seller.name, sum(tb_sales.amount) as total " +
                                    "FROM tb_seller " +
                                    "INNER JOIN tb_sales on tb_seller.id = tb_sales.seller_id " +
                                    "WHERE tb_sales.date BETWEEN :minDate AND :maxDate " +
                                    "GROUP BY tb_seller.name;";

    @Query( nativeQuery = true, value = SEARCH_BY_SELLER_QUERY )
    List<SellerAmountProjection> searchBySeller( LocalDate minDate, LocalDate maxDate );
}
