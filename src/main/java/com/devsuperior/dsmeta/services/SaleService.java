package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleSellerDTO;
import com.devsuperior.dsmeta.dto.SellerDTO;
import com.devsuperior.dsmeta.projections.SaleSellerProjection;
import com.devsuperior.dsmeta.projections.SellerAmountProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService
{
    @Autowired
    private SaleRepository repository;

    public SaleMinDTO findById( Long id )
    {
        Optional<Sale> result = repository.findById( id );
        Sale entity = result.get();
        return new SaleMinDTO( entity );
    }

    public Page<SaleSellerDTO> searchSalesReport( String minDateValue, String maxDateValue, String name, Pageable pageable )
    {
        Pair<LocalDate, LocalDate> datesRange = getDatesRange( minDateValue, maxDateValue );

        Page<SaleSellerProjection> saleSellerProjections = repository.searchSalesReport( datesRange.getFirst(), datesRange.getSecond(), name, pageable );

        return saleSellerProjections.map( SaleSellerDTO::new );
    }

    public List<SellerDTO> searchBySeller( String minDateValue, String maxDateValue )
    {
        Pair<LocalDate, LocalDate> datesRange = getDatesRange( minDateValue, maxDateValue );

        List<SellerAmountProjection> sellersAmountProjections = repository.searchBySeller( datesRange.getFirst(), datesRange.getSecond() );

        return sellersAmountProjections.stream().map( SellerDTO::new ).toList();
    }

    private Pair<LocalDate, LocalDate> getDatesRange( String minDateValue, String maxDateValue )
    {
        LocalDate minDate;
        LocalDate maxDate;

        if ( !minDateValue.isEmpty() && !maxDateValue.isEmpty() )
        {
            minDate = getLocalDateFromValue( minDateValue );
            maxDate = getLocalDateFromValue( maxDateValue );
        }

        else if ( minDateValue.isEmpty() && maxDateValue.isEmpty() )
        {
            maxDate = getCurrentLocalDate();
            minDate = getDateOneYearBack( maxDate );
        }

        else if ( minDateValue.isEmpty() )
        {
            maxDate = getLocalDateFromValue( maxDateValue );
            minDate = getDateOneYearBack( maxDate );
        }

        else
        {
            minDate = getLocalDateFromValue( minDateValue );
            maxDate = getCurrentLocalDate();
        }

        return Pair.of( minDate, maxDate );
    }

    private LocalDate getLocalDateFromValue( String dateValue )
    {
        return LocalDate.parse( dateValue );
    }

    private LocalDate getCurrentLocalDate()
    {
        return LocalDate.ofInstant( Instant.now(), ZoneId.systemDefault() );
    }

    private LocalDate getDateOneYearBack( LocalDate maxDate )
    {
        return maxDate.minusYears( 1 );
    }
}
