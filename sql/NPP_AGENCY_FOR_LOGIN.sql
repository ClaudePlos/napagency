-- oracle
CREATE TABLE NPP_AGENCY_FOR_LOGIN (
                                      ID           NUMBER,
                                      KL_KOD       VARCHAR2 (10),
                                      USER_ID      NUMBER,
                                      USER_NAME    VARCHAR2 (100),
                                      AGENCY_NAME  VARCHAR2 (200) ) ;


-- postgres
CREATE TABLE NPP_AGENCY_FOR_LOGIN (
                                      ID           numeric(10),
                                      KL_KOD       VARCHAR (10),
                                      USER_ID      numeric(10),
                                      USER_NAME    VARCHAR (100),
                                      AGENCY_NAME  VARCHAR (200) ) ;