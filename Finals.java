public class Finals {
    final static String SOCIETATI_PATH = "SOC.DBF";
    final static String VIEWS_PATH = "Views\\";
    final static String CONTDIC_PATH = "CONTDIC.DBF";
    final static String TVA_PATH = "TVA.DBF";


    final static String MAIN_STAGE_CAPTION = "Mijloc Fix";
    final static String SECIETATE_NU_EXISTA = "Societatea selectata nu exista!";
    final static String SECIETATE_NU_A_FOST_SELECTATA = "Societate nu fost selectata!";

//...............................Operatii
    final static String MIFIX_OP = "Mijloc fix";
    final static String ACHIZATIE_OP = "Achizatie";
    final static String VANZARE_OP = "Vanzare";
    final static String CASARE_OP = "Casare";
    final static String REEVALUARE_OP = "Reevaluare";
    final static String COMPLETARE_OP = "Completare";
    final static String AMENAJARE_OP = "Amenajara";
    final static String TRANSPORT_OP = "Transport";
//...............................Actions

    final static String ADAUGARE_OP = "Adaugare";
    final static String MODIFICARE_OP = "Modificare";
    final static String STERGERE_OP = "Stergere";

//..................................non numeric TVA procentage

    final static String SCUTIT_CU_DREPT = "Scutit cu drept de deducere";
    final static String SCUTIT_FARA_DREPT = "Scutit fara drept de deducere";
    final static String NEIMPOZABIL = "Neimpozabil";


//................................Alerts
    final static String SOCIETATE_DATABASE_NOT_SET_UP_TITLE_TEXT = "DB not set up";
    final static String SOCIETATE_DATABASE_NOT_SET_UP_CONTENT_TEXT = "DB for societatea selectta was not set up. Do you want to set it up?";

    final static String COMMON_DATABASE_NOT_SET_UP_TITLE_TEXT = "commonDataDB";
    final static String COMMON_DATABASE_NOT_SET_UP_HEADER_TEXT = "commonDataDB was not set up.";
    final static String COMMON_DATABASE_NOT_SET_UP_CONTENT_TEXT = "Do you want to set it up?";

    final static String SUCCESSFUL_OPERATION_TITLE_TEXT = "Success";
    final static String SUCCESSFUL_OPERATION_HEADER_TEXT = "Operatiune terminata cu success!";
    final static String SUCCESSFUL_OPERATION_CONTENT_TEXT = "";

    final static String MIFIX_TO_DELETE_HAS_OPERATIONS_TITLE_TEXT = "ATENTIE!";
    final static String MIFIX_TO_DELETE_HAS_OPERATIONS_HEADER_TEXT = "Mijloc fix are operatii!";
    final static String MIFIX_TO_DELETE_HAS_OPERATIONS_CONTENT_TEXT = "Vreti sa stergeti mijlocul fix cu toate operatii?";

    final static String INAPOI_BUTTON_TITLE_TEXT = "Unsaved work!";
    final static String INAPOI_BUTTON_HEADER_TEXT = "You didnt finish your Action. You will loose all unsaved work;";
    final static String INAPOI_BUTTON_CONTENT_TEXT = "Do you really want to exit?";

    final static String INVALID_INPUT_TITLE_TEXT = "Invalid input!";
    final static String INVALID_INPUT_CONTENT_TEXT = "Fix this!";

    //.......................creare mijloc fix
    final static String NR_INVENTAR_EXISTS_HEADER_TEXT = "Nr.inventar exista!";
    final static String NR_INVENTAR_DOSENT_EXISTS_HEADER_TEXT = "Nr.inventar nu exista!";
    final static String NR_INVENTAR_EMPTY_HEADER_TEXT = "Nr.inventar nu poate ramane gol!";
    final static String MI_FIX_SI_CAR_EMPTY_HEADER_TEXT = "Mijloc fix si caracteristice technice nu poate ramane gol!";
    final static String DURATA_AMORTIZARII_EMPTY_HEADER_TEXT = "Durata amortizarii nu poate ramane gol!";
    final static String MI_FIX_SI_CAR_TOO_LONG_HEADER_TEXT = "Mijloc fix si caracteristice technice cant be longer than 500 chars!";
    final static String COD_DE_CLASIFICARE_EMPTY_HEADER_TEXT = "Cod de clasificare nu poate ramane gol!";
    final static String COD_DE_CLASIFICARE_INCORECT_HEADER_TEXT = "Incorect cod de clasificare!";
    final static String INEXISTENT_CONT_DEBITOR_HEADER_TEXT = "Cont debitor nu exista!";
    final static String INEXISTENT_CONT_CREDITOR_HEADER_TEXT = "Cont creditor nu exista!";

    final static String NR_INVENTAR_NU_EXISTS_HEADER_TEXT = "Nr.inventar nu exista!";
    final static String VALOARE_FARA_TVA_EMPTY_HEADER_TEXT = "Valoare fara TVA nu poate ramane gol!";
    final static String PROCENT_TVA_EMPTY_HEADER_TEXT = "Procent TVA nu poate ramane gol!";

    //.........................basic operation
    final static String NR_RECEPTIE_EMPTY_HEADER_TEXT = "Nr. receptie nu poate ramane gol!";
    final static String FEL_DOCUMNENT_EMPTY_HEADER_TEXT = "Fel document nu poate ramane gol!";
    final static String NR_DOCUMENT_EMPTY_HEADER_TEXT = "Nr. document nu poate ramane gol!";


//.....................................SQL commands

    final static String SQL_COMMAND_DELIMITER = "----";

    final static String COMMON_DATABASE_NAME = "commonDataDB";

    final static String SET_QUOTES_SQL = "SET sql_mode='ANSI_QUOTES';";
    final static String SELECT_FROM_CLASIFICARI_SQL = "Select cod, description, minDur, maxDur from clasificari;";
    final static String SELECT_FROM_REGIMI_DE_AMORTIZARE_SQL = "Select denumire from regimiDeAmortizare;";

    final static String SELECT_FROM_MIJLOC_FIX_SQL = "Select nrInventar, mifixSiCaracteristiceTechnice, clasificare, durataAmortizarii, regimDeAmortizare, termenDeGarantie, contDebitor, contCreditor from mijlocFix;";

}
