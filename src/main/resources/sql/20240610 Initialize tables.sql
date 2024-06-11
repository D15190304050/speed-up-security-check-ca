DROP TABLE IF EXISTS `questionnaire_result`;

CREATE TABLE `questionnaire_result`
(
    `id`                                       BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID of the questionnaireResult result.',
    `email`                                    VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'Email of the user.',
    `application_type`                         INT                                                    DEFAULT NULL COMMENT 'Application type.',
    `application_submission_location`          INT                                                    DEFAULT NULL COMMENT 'Location where the user submits the application.',
    `application_submission_location_if_other` VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Text of the location if not listed (select "Other").',
    `country_of_residence`                     INT                                                    DEFAULT NULL COMMENT 'Current country of residence of the user.',
    `current_residence_if_other`               VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '',
    `current_passport_country`                 INT                                                    DEFAULT NULL COMMENT '',
    `current_passport_country_if_other`        VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '',
    `gender`                                   INT                                                    DEFAULT NULL COMMENT '',
    `gender_if_other`                          VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '',
    `application_submission_date`              DATE                                                   DEFAULT NULL COMMENT '',
    `application_end_date`                     DATE                                                   DEFAULT NULL COMMENT '',
    `date_of_entering_security_screening`      DATE                                                   DEFAULT NULL COMMENT '',
    `date_of_clearing_security_screening`      DATE                                                   DEFAULT NULL COMMENT '',
    `current_application_state`                INT                                                    DEFAULT NULL COMMENT '',
    `separated_with_family`                    BOOLEAN                                                DEFAULT NULL COMMENT '',
    `separation_with_family_member`            VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '',
    `separation_with_family_in_months`         BIGINT                                                 DEFAULT NULL COMMENT '',
    `delayed_enrollment`                       BOOLEAN                                                DEFAULT NULL COMMENT '',
    `original_school_start_date`               DATE                                                   DEFAULT NULL COMMENT '',
    `original_school_end_date`                 DATE                                                   DEFAULT NULL COMMENT '',
    `unable_to_change_job`                     BOOLEAN                                                DEFAULT NULL COMMENT '',
    `current_salary_in_cad_per_year`           DECIMAL(10, 2)                                         DEFAULT NULL COMMENT '',
    `estimated_salary_in_cad_per_year`         DECIMAL(10, 2)                                         DEFAULT NULL COMMENT '',
    `last_update_date_from_ircc`               DATETIME                                               DEFAULT NULL COMMENT ''
);