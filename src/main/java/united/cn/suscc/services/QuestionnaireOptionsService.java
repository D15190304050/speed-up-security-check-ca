package united.cn.suscc.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import united.cn.suscc.commons.ServiceResponse;
import united.cn.suscc.constants.LocaleCodes;
import united.cn.suscc.dao.QuestionnaireOptionMapper;
import united.cn.suscc.domain.dtos.LocaleQuestionnaireOption;
import united.cn.suscc.domain.dtos.QuestionnaireOption;
import united.cn.suscc.domain.dtos.QuestionnaireOptionsHolder;
import united.cn.suscc.domain.entities.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class QuestionnaireOptionsService
{
    @Value("${secret-for-updating}")
    private String secretForUpdating;

    @Autowired
    private QuestionnaireOptionMapper questionnaireOptionMapper;

    public List<LocaleQuestionnaireOption> getAllOptionsFromDb()
    {
        List<ApplicationState> allApplicationStates = questionnaireOptionMapper.getAllApplicationStates();
        List<ApplicationSubmissionLocation> allApplicationSubmissionLocations = questionnaireOptionMapper.getAllApplicationSubmissionLocations();
        List<ApplicationType> allApplicationTypes = questionnaireOptionMapper.getAllApplicationTypes();
        List<CountryOfResidence> allCountriesOfResidence = questionnaireOptionMapper.getAllCountriesOfResidence();
        List<CurrentPassportCountry> allCurrentPassportCountries = questionnaireOptionMapper.getAllCurrentPassportCountries();
        List<Gender> allGenders = questionnaireOptionMapper.getAllGenders();
        List<FamilyMember> allFamilyMembers = questionnaireOptionMapper.getAllFamilyMembers();

        List<LocaleQuestionnaireOption> localeQuestionnaireOptions = new ArrayList<>();
        for (String locale : LocaleCodes.ORDERED_LOCALES)
        {
            List<QuestionnaireOption> applicationStatesOptions = filterByLocale(locale, allApplicationStates);
            List<QuestionnaireOption> applicationSubmissionLocationsOptions = filterByLocale(locale, allApplicationSubmissionLocations);
            List<QuestionnaireOption> applicationTypesOptions = filterByLocale(locale, allApplicationTypes);
            List<QuestionnaireOption> countriesOfResidenceOptions = filterByLocale(locale, allCountriesOfResidence);
            List<QuestionnaireOption> currentPassportCountriesOptions = filterByLocale(locale, allCurrentPassportCountries);
            List<QuestionnaireOption> gendersOptions = filterByLocale(locale, allGenders);
            List<QuestionnaireOption> familyMemberOptions = filterByLocale(locale, allFamilyMembers);

            LocaleQuestionnaireOption localeQuestionnaireOption = new LocaleQuestionnaireOption();
            localeQuestionnaireOption.setLocale(locale);
            localeQuestionnaireOption.setApplicationStates(applicationStatesOptions);
            localeQuestionnaireOption.setApplicationSubmissionLocations(applicationSubmissionLocationsOptions);
            localeQuestionnaireOption.setApplicationTypes(applicationTypesOptions);
            localeQuestionnaireOption.setCountriesOfResidence(countriesOfResidenceOptions);
            localeQuestionnaireOption.setCurrentPassportCountries(currentPassportCountriesOptions);
            localeQuestionnaireOption.setGenders(gendersOptions);
            localeQuestionnaireOption.setFamilyMembers(familyMemberOptions);

            localeQuestionnaireOptions.add(localeQuestionnaireOption);
        }

        return localeQuestionnaireOptions;
    }

    private static <T extends QuestionnaireOptionBase> List<QuestionnaireOption> filterByLocale(String locale, List<T> options)
    {
        List<T> localeOptions = options.stream().filter(x -> x.getLocale().equalsIgnoreCase(locale)).toList();
        return QuestionnaireOption.toQuestionnaireOptions(localeOptions);
    }

    public ServiceResponse<List<LocaleQuestionnaireOption>> getAllOptionsFromCache()
    {
        return ServiceResponse.buildSuccessResponse(QuestionnaireOptionsHolder.getLocaleQuestionnaireOptions());
    }

    public ServiceResponse<Boolean> refreshOptions(String secret)
    {
        if (!StringUtils.hasText(secret))
            return ServiceResponse.buildErrorResponse(-2, "Please provide correct secret for refreshing options.");

        if (!secretForUpdating.equals(secret))
            return ServiceResponse.buildErrorResponse(-2, "Please provide correct secret for refreshing options.");

        List<LocaleQuestionnaireOption> allOptions = getAllOptionsFromDb();
        QuestionnaireOptionsHolder.setLocaleQuestionnaireOptions(allOptions);

        return ServiceResponse.buildSuccessResponse(true);
    }
}
