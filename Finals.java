public class Finals {
    final static String SOCIETATI_PATH = "SOC.DBF";
    final static String VIEWS_PATH = "Views\\";
    final static String CONTDIC_PATH = "CONTDIC.DBF";
    final static String TVA_PATH = "TVA.DBF";
    final static String SQL_QUERIES = "sqlqueries\\";


    final static String MAIN_STAGE_CAPTION = "Mijloc Fix";
    final static String SECIETATE_NU_EXISTA = "Societatea selectată nu există!";
    final static String SECIETATE_NU_A_FOST_SELECTATA = "Societatea nu fost selectată!";

//...............................Operatii
    final static String MIFIX_OP = "Mijloc fix";
    final static String ACHIZATIE_OP = "achizitie";
    final static String VANZARE_OP = "vanzare";
    final static String CASARE_OP = "casare";
    final static String REEVALUARE_OP = "reevaluare";
    final static String COMPLETARE_OP = "completare";
    final static String AMENAJARE_OP = "amenajare";
    final static String TRANSPORT_OP = "transport";
    final static String SUSPENDARE_OP = "suspendare";
    final static String AMORTIZARE_OP = "Amortizare";

//...............................VizualizareOptions

    final static String OPERATIUNI_VIZ_OP = "Operatiuni";
    final static String SUSPENDARI_VIZ_OP = "Suspendari";

//...............................Actions

    final static String ADAUGARE_OP = "Adaugare";
    final static String MODIFICARE_OP = "Modificare";
    final static String STERGERE_OP = "Stergere";

    final static String CALCULARE_OP = "Calculare amortizare";
    final static String RECALCULARE_OP = "Recalculare total";

//..................................non numeric TVA procentage

    final static String SCUTIT_CU_DREPT = "Scutit cu drept de deducere";
    final static String SCUTIT_FARA_DREPT = "Scutit fara drept de deducere";
    final static String NEIMPOZABIL = "Neimpozabil";


//................................Alerts
    final static String SOCIETATE_DATABASE_NOT_SET_UP_TITLE_TEXT = "Nu a fost creat bază de date!";
    final static String SOCIETATE_DATABASE_NOT_SET_UP_CONTENT_TEXT = "Bază de dată al societații selectate nu există! Doriți să creați??";

    final static String COMMON_DATABASE_NOT_SET_UP_TITLE_TEXT = "commonDataDB";
    final static String COMMON_DATABASE_NOT_SET_UP_HEADER_TEXT = "commonDataDB nu a fost creată!";
    final static String COMMON_DATABASE_NOT_SET_UP_CONTENT_TEXT = "Doriți să creați?";

    final static String SUCCESSFUL_OPERATION_TITLE_TEXT = "Succes";
    final static String SUCCESSFUL_OPERATION_HEADER_TEXT = "Operațiune terminată cu success!";
    final static String SUCCESSFUL_OPERATION_CONTENT_TEXT = "";

    final static String MIFIX_TO_DELETE_HAS_OPERATIONS_TITLE_TEXT = "ATENȚIE!";
    final static String MIFIX_TO_DELETE_HAS_OPERATIONS_HEADER_TEXT = "Mijloc fix are operații!";
    final static String MIFIX_TO_DELETE_HAS_OPERATIONS_CONTENT_TEXT = "Vreți să ștergeți mijlocul fix cu datele existente?";

    final static String INAPOI_BUTTON_TITLE_TEXT = "Date nesalvate!";
    final static String INAPOI_BUTTON_HEADER_TEXT = "Acțiune neterminată! Datele nesalvate se vor pierde!";
    final static String INAPOI_BUTTON_CONTENT_TEXT = "Doriți să iesiți?";

    final static String INVALID_INPUT_TITLE_TEXT = "Intrare greșită!"; // invalid input
    final static String INVALID_INPUT_CONTENT_TEXT = "Fără corectarea celulelor greșite, datele introduse se vor pierde!";//fix this

    //.......................creare mijloc fix

    final static String NR_INVENTAR_EXISTS_HEADER_TEXT = "Nr.inventar există!";
    final static String NR_INVENTAR_DOSENT_EXISTS_HEADER_TEXT = "Nr.inventar nu există!";
    final static String SELECTED_NR_INVENTAR_DOSENT_EXISTS_HEADER_TEXT = "Nr.inventar seletată nu există!";
    final static String NR_INVENTAR_EMPTY_HEADER_TEXT = "Nr.inventar nu poate ramâne gol!";
    final static String DATE_EMPTY_HEADER_TEXT = "Date nu pot ramân gol!";
    final static String MI_FIX_SI_CAR_EMPTY_HEADER_TEXT = "Mijloc fix și caracteristice technice nu poate ramâne gol!";
    final static String DURATA_AMORTIZARII_EMPTY_HEADER_TEXT = "Durata amortizării nu poate ramâne gol!";
    final static String MI_FIX_SI_CAR_TOO_LONG_HEADER_TEXT = "Mijloc fix și caracteristice technice nu poate depăsi 500 de caractere!";
    final static String COD_DE_CLASIFICARE_EMPTY_HEADER_TEXT = "Cod de clasificare nu poate ramâne gol!";
    final static String COD_DE_CLASIFICARE_INCORECT_HEADER_TEXT = "Cod de clasificare incorectă!";
    final static String INCEPUTUL_AMORTIZARII_EMPTY_HEADER_TEXT = "Inceputul amortizarii nu poate ramane gol!";
    final static String STARTING_DATE_CANT_BE_BEFORE_START_OF_AMORTIZARE_HEADER = "Starting date cant be before inceputul amortizarii of mifix!";

    final static String INEXISTENT_CONT_DEBITOR_HEADER_TEXT = "Cont debitor nu există!";
    final static String INEXISTENT_CONT_CREDITOR_HEADER_TEXT = "Cont creditor nu există!";

    final static String NR_INVENTAR_NU_EXISTS_HEADER_TEXT = "Nr.inventar nu există!";
    final static String VALOARE_FARA_TVA_EMPTY_HEADER_TEXT = "Valoare fără TVA nu poate ramâne gol!";
    final static String PROCENT_TVA_EMPTY_HEADER_TEXT = "Procent TVA nu poate ramâne gol!";

    final static String MIFIX_NOt_SELECtED_TEXT = "Mijloc fix nu a fost selectat!";

    //......modificare mijlocfix

    final static String MODIFICARE_HEADER_TEXT = "Modificare!";
    final static String MODIFICARE_MIJLOC_FIX_TITLE_TEXT = "Modificare mijloc fix!";

    //.........................basic operation
    final static String NR_DOCUMENT_EMPTY_HEADER_TEXT = "Nr. document nu poate ramâne gol!";
    final static String NR_RECEPTIE_EMPTY_HEADER_TEXT = "Nr. receptie nu poate ramâne gol!";
    final static String FEL_DOCUMNENT_EMPTY_HEADER_TEXT = "Fel document nu poate ramâne gol!";

    //...........modificare/stergere operatie

    final static String OPERATIE_NOT_SELECTED_HEADER = "Operație nu a fost selectată!";

    //....amortizare

    final static String START_DATE_AFTER_END_HEADER = "Date of start cant be befor end date";

//.....................................SQL commands

    final static String SQL_COMMAND_DELIMITER = "----";

    final static String COMMON_DATABASE_NAME = "commonDataDB";

    final static String SET_QUOTES_SQL = "SET sql_mode='ANSI_QUOTES';";
    final static String START_TRANSACTION = "START TRANSACTION;";
    final static String COMMIT_TRANSACTION = "COMMIT;";
    final static String SELECT_FROM_CLASIFICARI_SQL = "Select cod, description, minDur, maxDur from clasificari;";
    final static String SELECT_FROM_REGIMI_DE_AMORTIZARE_SQL = "Select denumire from regimiDeAmortizare;";
    final static String SELECT_FELURI_DE_OPERATII_SQL = "Select denumire from commonDataDB.feluriOperatiei;";
    final static String SELECT_VALORI_FOR_OPERATION_SQL = "Select procentTVAID, procentTVA, valoareFaraTVA, diferentaTVA from operatieValori where operatieID = ?;";
    final static String DELETE_OPERATION_SQL = "delete from operatieBase where operatieID = ?;";

    final static String SELECT_FROM_MIJLOC_FIX_SQL = "Select nrInventar, mifixSiCaracteristiceTechnice, clasificare, inceputulAmortizarii, durataAmortizarii, regimDeAmortizare, termenDeGarantie, contDebitor, contCreditor from mijlocFix;";
    final static String UPDATE_OPERATIE_BASE_SQL = "update operatieBase set mifixID = (Select mijlocFix.mifixID from mijlocFix where nrInventar = ?), nrReceptie = ?, felDocument = ?, nrDocument = ?, dataOperatiei = ?, felOperatieiID = (Select commonDataDB.felurioperatiei.felOperatieiID from commondatadb.felurioperatiei where denumire= ? ) where operatieID = ?;";
    final static String DELETE_VALORI_SQL = "delete from operatieValori where operatieID = ?;";

    final static String INSERT_IN_REEVALUARE_SQL = "insert into reevaluari (operatieID, newValue) VALUES ((Select MAX(operatieID) from operatiebase),?);";
    final static String MODIFICARE_REEVALUARE_SQL = "update reevaluari set newValue = ? where operatieID = ?;";
    final static String DELETE_REEVALUARE_SQL = "delete reevaluari where operatieID = ?;";

    final static String INSERT_IN_SUSPENDARE_SQL = "insert into suspendari (mifixID, startDate, endDate) VALUES ((Select mifixID from mijlocFix where nrInventar=?),?,?);";
    final static String UPDATE_SUSPENDARE_SQL = "update suspendari set  mifixID = (Select mifixID from mijlocFix where nrInventar = ?), startDate = ?, endDate = ? where suspendareID = ?;";
    final static String DELETE_SUSPENDARE_SQL = "delete from suspendari where suspendareID = ?;";

    //amortizare sql

    final static String GET_ALL_OPERATIE_OF_MIFIX_SQL = "select operatiebase.operatieID as opID, commonDataDB.feluriOperatiei.denumire as felOperatieidenumire, dataOperatiei, sum(valoareFaraTVA) as valoareFaraTVASum " +
            "from mijlocFix join operatiebase on mijlocFix.mifixID = operatiebase.mifixID " +
            "Left Join operatievalori on operatiebase.operatieID = operatieValori.operatieID " +
            "Join commonDataDB.feluriOperatiei on commonDataDB.feluriOperatiei.felOperatieiID = operatiebase.felOperatieiID " +
            "where mijlocFix.nrInventar = ?" +
            "group by operatiebase.operatieID " +
            "order by dataOperatiei;";

    final static String NOT_CALCULATED_IN_A_MONTH_SQL = "select mifixID from mijlocFix where mifixID NOT IN (select mifixID from amortizare where monthOfAmortizare = ?);";
    final static String REEVALUARE_VALUE_SQL = "select newValue from reevaluari where operatieID = ?;";
    final static String IS_SUSPENDED_SQL = "select suspendareID from suspendari " +
            "where mifixID = (select mifixID from mijlocFix where nrInventar = ?) and DATE(?) between startDate and endDate;";
    final static String IS_AMORTIZAT_SQL = "select amortizareID from amortizare " +
            "where mifixID = (select mifixID from mijlocFix where nrInventar = ?) and monthOfAmortizare.MONTH() = Date(?).MONTH() and monthOfAmortizare.YEAR() = Date(?).YEAR();";
    final static String AMORTIZAT_VALUE_UNTIL_SQL = "select sum(calculatedValue) as calcSum, sum(diferenta) as diffSum from amortizare " +
            "where mifixID = (select mifixID from mijlocFix where nrInventar = ?) and monthOfAmortizare < DATE(?);";
    final static String INSERT_INTO_AMORTIZARE_SQL = "insert into amortizare (mifixID, monthOfAmortizare, calculatedValue, diferenta) VALUES ((select mifixID from mijlocFix where nrInventar = ?),?,?,?);";
    final static String GET_AMORTIZATION_START_DATE_SQL = "select inceputulAmortizarii from mijlocFix where nrInventar = ?;";
    final static String NR_OF_AMORTIZAT_MONTHS_SQL = "select count(amortizareID) as monthCount from amortizari where mifixID = (select mifixID from mijlocFix where nrInventar = ?);";
    final static String LAST_AMORTIZATION_DATE_SQL = "select MAX(monthOfAmortizare) as lastMonth from amortizari where mifixID = (select mifixID from mijlocFix where nrInventar = ?);";
}
