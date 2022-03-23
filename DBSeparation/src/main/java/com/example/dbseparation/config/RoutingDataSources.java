package com.example.dbseparation.config;

import com.example.dbseparation.exception.RoutingDataSourceLookupException;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *   application.yml, application-xxx.yml 에
 *   routing.datasource.use 존재 여부 및 True/False Check하여
 *   RoutingDataSource 생성 할지 판단 후 생성한다.
 *   RoutingDataSource 를 미생성시 기본 application.yml 데이터소스 속성으로 셋팅 된다.
 *   특이사항) pay 는 Spring Cloud Config 사용하지 않아 resources/application.yml 의
 *           데이터소스를 사용하여 outing.datasource.use 처리를 함
 *   참고) mcare4 Spring Cloud Config 컨피를 사용함. 꼭 이론 공부 할 것.
 * </pre>
 * @author hyunmin.kim
 * @since 202101
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "routing.datasource")
@ConditionalOnProperty(prefix = "routing.datasource", name = "use")
public class RoutingDataSources extends AbstractRoutingDataSource implements InitializingBean {

    /**
     * 기본 데이터 소스
     * TODO : master 처리를 어떻게 할 것인지 논의 필요!
     */
    @Setter @Getter
    private RoutingDataSource master;

    /**
     * 라우팅될 데이터소스 리스트
     */
    @Setter @Getter
    private List<RoutingDataSource> list;

    /**
     * bean 생성 될때 AbstractRoutingDataSource 셋팅한다.
     */
    @Override
    public void afterPropertiesSet() {
        setDefaultTargetDataSource();
        setTargetDataSources();
        super.afterPropertiesSet();
    }

    /**
     * <pre>
     *   기본 데이터소스 셋팅
     *   기본 데이터소스를 셋팅을 하지 않으면
     *   라우팅 대상이 없을때 오류가 발생 된다.
     * </pre>
     */
    public void setDefaultTargetDataSource(){
        DataSourceBuilder dsb = DataSourceBuilder.create().type(HikariDataSource.class);
        dsb.driverClassName(master.getDriverClassName());
        dsb.url(master.getUrl());
        dsb.username(master.getUsername());
        dsb.password(master.getPassword());
        super.setDefaultTargetDataSource(dsb.build());
    }

    /**
     * <pre>
     *   라우팅될 데이터소스 셋팅
     * </pre>
     */
    public void setTargetDataSources(){
        Map<Object, Object> map = new HashMap<>();
        for(RoutingDataSource item : list){
           DataSourceBuilder dsb = DataSourceBuilder.create().type(HikariDataSource.class);
           dsb.driverClassName(item.getDriverClassName());
           dsb.url(item.getUrl());
           dsb.username(item.getUsername());
           dsb.password(item.getPassword());
           map.put(item.getHospitalCd(), dsb.build());
        }
        super.setTargetDataSources(map);
    }

    /**
     * <pre>
     *   라우팅 데이터소스의 키를 구하는 메소드
     *   return null 경우는 기본 데이터소스를 사용한다.
     * </pre>
     * @return hospitalCd
     */
    @Override
    protected Object determineCurrentLookupKey() throws RoutingDataSourceLookupException {

        ServletRequestAttributes sra = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());

        // 최초 SpringBoot 가 기동되는 시점이라고 판단
        // 사유 : sra가 null 경우 SpringBoot 기동 시 에러가 발생(EntityManagerFactory 생성시)
        // 정말 기동 시점 체크하려면, CommandLineRunner, ApplicationRunner 를 사용해서
        // 구분할 수 있는 플래그? 등등 처리가 필요
        String hospitalCd;
        if(sra == null){
           return null;
        }
        else if( !StringUtils.isEmpty(sra.getRequest().getHeader("hospitalCd"))
            && sra.getRequest().getHeader("hospitalCd").equalsIgnoreCase("commondb") ){ // 관리자 페이지 병원 조회를 위한 통합디비 접근
            return null;
        }
        else if( !StringUtils.isEmpty((hospitalCd = sra.getRequest().getHeader("hospitalCd"))) ) { // 헤더값 조회
            return hospitalCd;
        }
        else if( !StringUtils.isEmpty((hospitalCd = sra.getRequest().getParameter("hospitalCd"))) ){ // 병원코드 존재
            return hospitalCd;
        }
        else{ // 병원코드 미존재
            String reqUrl = sra.getRequest().getRequestURI();
            throw new RoutingDataSourceLookupException(String.format("%s 요청 헤더에 병원코드가 존재하지 않습니다.", reqUrl));
        }
    }

    /**
     * <pre>
     *   ConfigurationProperties의 데이서 소스 정보를 맵핑하는 내부 클래스
     * </pre>
     */
    @Data
    @Configuration
    public static class RoutingDataSource{
        String hospitalCd;
        String driverClassName;
        String url;
        String username;
        String password;
    }

}
