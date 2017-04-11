CREATE VIEW VIEW_USER_INFORMATION AS
SELECT CAST(U.USER_ID AS VARCHAR2(100)) AS USER_ID,U.FIRST_NAME,U.LAST_NAME,U.YEAR_OF_BIRTH,U.MONTH_OF_BIRTH,U.DAY_OF_BIRTH,U.GENDER,
C1.CITY_NAME AS CURRENT_CITY,C1.STATE_NAME AS CURRENT_STATE,C1.COUNTRY_NAME AS CURRENT_COUNTRY,
C2.CITY_NAME AS HOMETOWN_CITY,C2.STATE_NAME AS HOMETOWN_STATE,C2.COUNTRY_NAME AS HOMETOWN_COUNTRY,
P.INSTITUTION AS INSTITUTION_NAME,E.PROGRAM_YEAR,CAST(P.CONCENTRATION AS CHAR(100))AS PROGRAM_CONCENTRATION,P.DEGREE AS PROGRAM_DEGREE
FROM USERS U
LEFT JOIN USER_CURRENT_CITY CC ON U.USER_ID=CC.USER_ID
LEFT JOIN USER_HOMETOWN_CITY HC ON U.USER_ID=HC.USER_ID
LEFT JOIN CITIES C1 ON C1.CITY_ID=CC.CURRENT_CITY_ID
LEFT JOIN CITIES C2 ON C2.CITY_ID=HC.HOMETOWN_CITY_ID
LEFT JOIN EDUCATION E ON U.USER_ID=E.USER_ID
LEFT JOIN PROGRAMS P ON P.PROGRAM_ID=E.PROGRAM_ID;


CREATE VIEW VIEW_ARE_FRIENDS AS
SELECT CAST(F.USER1_ID AS VARCHAR2(100)) AS USER1_ID,CAST(F.USER2_ID AS VARCHAR2(100)) AS USER2_ID
FROM FRIENDS F;

CREATE VIEW VIEW_PHOTO_INFORMATION AS
SELECT A.ALBUM_ID,CAST(A.ALBUM_OWNER_ID AS VARCHAR2(100)) AS OWNER_ID,A.COVER_PHOTO_ID,A.ALBUM_NAME,A.ALBUM_CREATED_TIME,A.ALBUM_MODIFIED_TIME,A.ALBUM_LINK,A.ALBUM_VISIBILITY, 
P.PHOTO_ID,P.PHOTO_CAPTION,P.PHOTO_CREATED_TIME,P.PHOTO_MODIFIED_TIME,P.PHOTO_LINK
FROM ALBUMS A, PHOTOS P
WHERE A.ALBUM_ID=P.ALBUM_ID;

CREATE VIEW VIEW_TAG_INFORMATION AS
SELECT TAG_PHOTO_ID AS PHOTO_ID,CAST(TAG_SUBJECT_ID AS VARCHAR2(100)) AS TAG_SUBJECT_ID,TAG_CREATED_TIME,TAG_X AS TAG_X_COORDINATE,TAG_Y AS TAG_Y_COORDINATE
FROM TAGS;

CREATE VIEW VIEW_EVENT_INFORMATION AS
SELECT CAST(E.EVENT_ID AS VARCHAR2(100)) AS EVENT_ID,CAST(E.EVENT_CREATOR_ID AS VARCHAR2(100)) AS EVENT_CREATOR_ID,
E.EVENT_NAME,CAST(E.EVENT_TAGLINE AS VARCHAR2(1000)) AS EVENT_TAGLINE
,CAST(E.EVENT_DESCRIPTION AS VARCHAR2(4000)) AS EVENT_DESCRIPTION,E.EVENT_HOST,E.EVENT_TYPE,
E.EVENT_SUBTYPE,E.EVENT_LOCATION,C.CITY_NAME AS EVENT_CITY,C.STATE_NAME AS EVENT_STATE,C.COUNTRY_NAME AS EVENT_COUNTRY,
E.EVENT_START_TIME,E.EVENT_END_TIME
FROM  USER_EVENTS E, CITIES C
WHERE E.EVENT_CITY_ID=C.CITY_ID;


