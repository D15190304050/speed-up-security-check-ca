package united.cn.suscc.dao;

import org.apache.ibatis.annotations.Mapper;
import united.cn.suscc.domain.entities.*;

import java.util.List;

@Mapper
public interface QuestionnaireOptionMapper
{
    List<ApplicationState> getAllApplicationStates();
    List<ApplicationSubmissionLocation> getAllApplicationSubmissionLocations();
    List<ApplicationType> getAllApplicationTypes();
    List<CountryOfResidence> getAllCountriesOfResidence();
    List<CurrentPassportCountry> getAllCurrentPassportCountries();
    List<Gender> getAllGenders();
}
