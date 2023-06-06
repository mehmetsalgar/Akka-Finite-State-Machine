package org.salgar.fsm.pekko.foureyes.credit.actions.config;

import org.salgar.fsm.pekko.foureyes.addresscheck.facade.AdressCheckSMFacade;
import org.salgar.fsm.pekko.foureyes.credit.actions.ADRRESCHECK_RESULT_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_addressCheck_CredfitScore_onResultReceived_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.ADRRESCHECK_RESULT_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_addressCheck_CredfitScore_onResultReceived_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.ADRRESCHECK_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_addressCheckResultReceived_fraudPreventionResultReceived_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.ADRRESCHECK_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_addressCheckResultReceived_fraudPreventionResultReceived_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.CREDITSCORE_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_creditScore_addressCheck_onResultReceived_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.CREDITSCORE_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_creditScore_addressCheck_onResultReceived_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.CREDITSCORE_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_creditScoreReceived_creditScore_fraudPreventionResult_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.CREDITSCORE_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_creditScoreReceived_creditScore_fraudPreventionResult_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updated_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updated_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceived_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceived_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejected_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejected_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.FRAUDPREVENTION_RESULT_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_fraudPreventionResultReceived_creditScoreFraudPreventionResultReceived_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.FRAUDPREVENTION_RESULT_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_fraudPreventionResultReceived_creditScoreFraudPreventionResultReceived_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.FRAUDPREVENTION_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_fraudPReventionResultReceived_fraudPReventionResultReceived_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.FRAUDPREVENTION_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_fraudPReventionResultReceived_fraudPReventionResultReceived_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_$$_CREDIT_APPLICATION_SUBMITTED_intial_onSubmit_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_$$_CREDIT_APPLICATION_SUBMITTED_intial_onSubmit_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_CA_$$_WAITING_APPROVAL_inital_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_CA_$$_WAITING_APPROVAL_inital_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_CA_SM_$$_WAITING_ANAYLIST_APPROVAL_initial_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_CA_SM_$$_WAITING_ANAYLIST_APPROVAL_initial_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_CSC_$$_ADRRESCHECK_RESULT_RECEIVED_initial_AddressCheckResultReceived_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_CSC_$$_ADRRESCHECK_RESULT_RECEIVED_initial_AddressCheckResultReceived_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_CSC_$$_CREDITSCORE_RECEIVED_initial_creditScoreReceived_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_CSC_$$_CREDITSCORE_RECEIVED_initial_creditScoreReceived_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_CSC_$$_FRAUDPREVENTION_RESULT_RECEIVED_initial_FraudPreventionResultReceived_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_CSC_$$_FRAUDPREVENTION_RESULT_RECEIVED_initial_FraudPreventionResultReceived_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_FE_$$_WAITING_MANAGER_APPROVAL_initial_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_FE_$$_WAITING_MANAGER_APPROVAL_initial_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_FE_SM_$$_WAITING_MANAGER_APPROVAL_intial_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.INITIAL_FE_SM_$$_WAITING_MANAGER_APPROVAL_intial_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.RELATIONSHIP_MANAGER_APPROVED_$$_SALES_MANAGER_APPROVED_relationshipManagerApproved_onSalesManagerApproved_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.RELATIONSHIP_MANAGER_APPROVED_$$_SALES_MANAGER_APPROVED_relationshipManagerApproved_onSalesManagerApproved_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_salesManagerApproved_onCreditScoreReceived_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_salesManagerApproved_onCreditScoreReceived_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceived_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceived_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_waitingAnalystApproval_onCreditAccepted_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_waitingAnalystApproval_onCreditAccepted_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.WAITING_APPROVAL_$$_RELATIONSHIP_MANAGER_APPROVED_waitingApproval_onRelationshipManagerApproved_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.WAITING_APPROVAL_$$_RELATIONSHIP_MANAGER_APPROVED_waitingApproval_onRelationshipManagerApproved_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.WAITING_APPROVAL_FROM_SENIOR_MANAGER_$$_WAITING_CREDIT_ANALYST_APPROVAL_waitingApprovalFromSeniorOfficier_onAcceptableScore_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.WAITING_APPROVAL_FROM_SENIOR_MANAGER_$$_WAITING_CREDIT_ANALYST_APPROVAL_waitingApprovalFromSeniorOfficier_onAcceptableScore_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.WAITING_CREDIT_ANALYST_APPROVAL_$$_CREDIT_ACCEPTED_creditAnalyst_onAccepted_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.WAITING_CREDIT_ANALYST_APPROVAL_$$_CREDIT_ACCEPTED_creditAnalyst_onAccepted_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingForApproval_onSalesManagerApproved_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingForApproval_onSalesManagerApproved_ActionImpl;
import org.salgar.fsm.pekko.foureyes.credit.actions.WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingManagerApproval_onRelationshipManagerApproved_Action;
import org.salgar.fsm.pekko.foureyes.credit.actions.WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingManagerApproval_onRelationshipManagerApproved_ActionImpl;
import org.salgar.fsm.pekko.foureyes.creditscore.facade.MultiTenantCreditScoreSMFacade;
import org.salgar.fsm.pekko.foureyes.fraudprevention.facade.FraudPreventionSMFacade;
import org.salgar.pekko.fsm.foureyes.cra.kafka.CustomerRelationshipAdapter;
import org.salgar.pekko.fsm.foureyes.notifier.NotifierService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreditSMActionConfiguration {
    @Bean
    public RELATIONSHIP_MANAGER_APPROVED_$$_SALES_MANAGER_APPROVED_relationshipManagerApproved_onSalesManagerApproved_Action
        creditsm_relationship_manager_approved_SALES_MANAGER_APPROVED_relationshipManagerApproved_onSalesManagerApprovedAction(
            MultiTenantCreditScoreSMFacade multiTenantCreditScoreSMFacade,
            FraudPreventionSMFacade fraudPreventionSMFacade,
            AdressCheckSMFacade addressCheckSMFacade) {
        return new RELATIONSHIP_MANAGER_APPROVED_$$_SALES_MANAGER_APPROVED_relationshipManagerApproved_onSalesManagerApproved_ActionImpl(
                multiTenantCreditScoreSMFacade,
                fraudPreventionSMFacade,
                addressCheckSMFacade);
    }
    @Bean
    public ADRRESCHECK_RESULT_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_addressCheck_CredfitScore_onResultReceived_Action
        creditsm_adrrescheck_result_received_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_addressCheck_CredfitScore_onResultReceivedAction() {
            return new ADRRESCHECK_RESULT_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_addressCheck_CredfitScore_onResultReceived_ActionImpl();
    }
    @Bean
    public ADRRESCHECK_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_addressCheckResultReceived_fraudPreventionResultReceived_Action
        creditsm_adrrescheck_result_received_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_addressCheckResultReceived_fraudPreventionResultReceivedAction() {
        return new ADRRESCHECK_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_addressCheckResultReceived_fraudPreventionResultReceived_ActionImpl();
    }
    @Bean
    public CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceived_Action
        creditsm_credit_application_submitted_CREDIT_REJECTED_lowScore_onResultReceivedAction() {
        return new CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_lowScore_onResultReceived_ActionImpl();
    }
    @Bean
    public CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejected_Action
        creditsm_credit_application_submitted_CREDIT_REJECTED_managerRejected_onRejectedAction() {
        return new CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_REJECTED_managerRejected_onRejected_ActionImpl();
    }
    @Bean
    public CREDITSCORE_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_creditScoreReceived_creditScore_fraudPreventionResult_Action
        creditsm_creditscore_received_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_creditScoreReceived_creditScore_fraudPreventionResultAction() {
        return new CREDITSCORE_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_creditScoreReceived_creditScore_fraudPreventionResult_ActionImpl();
    }
    @Bean
    public CREDITSCORE_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_creditScore_addressCheck_onResultReceived_Action
        creditsm_creditscore_received_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_creditScore_addressCheck_onResultReceivedAction() {
        return new CREDITSCORE_RECEIVED_$$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED_creditScore_addressCheck_onResultReceived_ActionImpl();
    }
    @Bean
    public CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updated_Action
        creditsm_credit_application_submitted_CREDIT_APPLICATION_SUBMITTED_customer_updatedAction() {
        return new CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_APPLICATION_SUBMITTED_customer_updated_ActionImpl();
    }
    @Bean
    public FRAUDPREVENTION_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_fraudPReventionResultReceived_fraudPReventionResultReceived_Action
        creditsm_fraudprevention_result_received_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_fraudPReventionResultReceived_fraudPReventionResultReceivedAction() {
        return new FRAUDPREVENTION_RESULT_RECEIVED_$$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED_fraudPReventionResultReceived_fraudPReventionResultReceived_ActionImpl();
    }
    @Bean
    public FRAUDPREVENTION_RESULT_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_fraudPreventionResultReceived_creditScoreFraudPreventionResultReceived_Action
        creditsm_fraudprevention_result_received_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_fraudPreventionResultReceived_creditScoreFraudPreventionResultReceivedAction() {
        return new FRAUDPREVENTION_RESULT_RECEIVED_$$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED_fraudPreventionResultReceived_creditScoreFraudPreventionResultReceived_ActionImpl();
    }
    @Bean
    public SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_salesManagerApproved_onCreditScoreReceived_Action
        creditsm_sales_manager_approved_WAITING_APPROVAL_FROM_SENIOR_MANAGER_salesManagerApproved_onCreditScoreReceivedAction(
            NotifierService notifierService) {
        return new SALES_MANAGER_APPROVED_$$_WAITING_APPROVAL_FROM_SENIOR_MANAGER_salesManagerApproved_onCreditScoreReceived_ActionImpl(notifierService);
    }
    @Bean
    public SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceived_Action
        creditsm_sales_manager_approved_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceivedAction(
            NotifierService notifierService) {
        return new SALES_MANAGER_APPROVED_$$_WAITING_CREDIT_ANALYST_APPROVAL_salesManagerApproved_onResultReceived_ActionImpl(notifierService);
    }
    @Bean
    public WAITING_APPROVAL_FROM_SENIOR_MANAGER_$$_WAITING_CREDIT_ANALYST_APPROVAL_waitingApprovalFromSeniorOfficier_onAcceptableScore_Action
        creditsm_waiting_approval_from_senior_manager_WAITING_CREDIT_ANALYST_APPROVAL_waitingApprovalFromSeniorOfficier_onAcceptableScoreAction(
            NotifierService notifierService) {
        return new WAITING_APPROVAL_FROM_SENIOR_MANAGER_$$_WAITING_CREDIT_ANALYST_APPROVAL_waitingApprovalFromSeniorOfficier_onAcceptableScore_ActionImpl(notifierService);
    }

    @Bean
    public WAITING_APPROVAL_$$_RELATIONSHIP_MANAGER_APPROVED_waitingApproval_onRelationshipManagerApproved_Action
        creditsm_waiting_approval_RELATIONSHIP_MANAGER_APPROVED_waitingApproval_onRelationshipManagerApprovedAction(
            NotifierService notifierService) {
        return new WAITING_APPROVAL_$$_RELATIONSHIP_MANAGER_APPROVED_waitingApproval_onRelationshipManagerApproved_ActionImpl(notifierService);
    }
    @Bean
    public INITIAL_$$_CREDIT_APPLICATION_SUBMITTED_intial_onSubmit_Action
        creditsm_initial_CREDIT_APPLICATION_SUBMITTED_intial_onSubmitAction(
            CustomerRelationshipAdapter customerRelationshipAdapter,
            NotifierService notifierService) {
        return new INITIAL_$$_CREDIT_APPLICATION_SUBMITTED_intial_onSubmit_ActionImpl(
                customerRelationshipAdapter,
                notifierService);
    }
    @Bean
    public INITIAL_CA_$$_WAITING_APPROVAL_inital_Action creditsm_initial_ca_WAITING_APPROVAL_initalAction() {
        return new INITIAL_CA_$$_WAITING_APPROVAL_inital_ActionImpl();
    }
    @Bean
    public INITIAL_CA_SM_$$_WAITING_ANAYLIST_APPROVAL_initial_Action creditsm_initial_ca_sm_WAITING_ANAYLIST_APPROVAL_initialAction() {
        return new INITIAL_CA_SM_$$_WAITING_ANAYLIST_APPROVAL_initial_ActionImpl();
    }
    @Bean
    public INITIAL_FE_$$_WAITING_MANAGER_APPROVAL_initial_Action creditsm_initial_fe_WAITING_MANAGER_APPROVAL_initialAction() {
        return new INITIAL_FE_$$_WAITING_MANAGER_APPROVAL_initial_ActionImpl();
    }
    @Bean
    public INITIAL_FE_SM_$$_WAITING_MANAGER_APPROVAL_intial_Action creditsm_initial_fe_sm_WAITING_MANAGER_APPROVAL_intialAction() {
        return new INITIAL_FE_SM_$$_WAITING_MANAGER_APPROVAL_intial_ActionImpl();
    }
    @Bean
    public INITIAL_CSC_$$_ADRRESCHECK_RESULT_RECEIVED_initial_AddressCheckResultReceived_Action
        creditsm_initial_csc_ADRRESCHECK_RESULT_RECEIVED_initial_AddressCheckResultReceivedAction() {
        return new INITIAL_CSC_$$_ADRRESCHECK_RESULT_RECEIVED_initial_AddressCheckResultReceived_ActionImpl();
    }
    @Bean
    public INITIAL_CSC_$$_FRAUDPREVENTION_RESULT_RECEIVED_initial_FraudPreventionResultReceived_Action
        creditsm_initial_csc_FRAUDPREVENTION_RESULT_RECEIVED_initial_FraudPreventionResultReceivedAction() {
        return new INITIAL_CSC_$$_FRAUDPREVENTION_RESULT_RECEIVED_initial_FraudPreventionResultReceived_ActionImpl();
    }
    @Bean
    public INITIAL_CSC_$$_CREDITSCORE_RECEIVED_initial_creditScoreReceived_Action
        creditsm_initial_csc_CREDITSCORE_RECEIVED_initial_creditScoreReceivedAction() {
        return new INITIAL_CSC_$$_CREDITSCORE_RECEIVED_initial_creditScoreReceived_ActionImpl();
    }
    @Bean
    public WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_waitingAnalystApproval_onCreditAccepted_Action
        creditsm_waiting_anaylist_approval_WAITING_ANAYLIST_APPROVAL_waitingAnalystApproval_onCreditAcceptedAction() {
        return new WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_waitingAnalystApproval_onCreditAccepted_ActionImpl();
    }
    @Bean
    public WAITING_CREDIT_ANALYST_APPROVAL_$$_CREDIT_ACCEPTED_creditAnalyst_onAccepted_Action
        creditsm_waiting_credit_analyst_approval_CREDIT_ACCEPTED_creditAnalyst_onAcceptedAction(NotifierService notifierService) {
        return new WAITING_CREDIT_ANALYST_APPROVAL_$$_CREDIT_ACCEPTED_creditAnalyst_onAccepted_ActionImpl(notifierService);
    }
    @Bean
    public WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingForApproval_onSalesManagerApproved_Action
        creditsm_waiting_manager_approval_WAITING_MANAGER_APPROVAL_waitingForApproval_onSalesManagerApprovedAction() {
        return new WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingForApproval_onSalesManagerApproved_ActionImpl();
    }
    @Bean
    public WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingManagerApproval_onRelationshipManagerApproved_Action
        creditsm_waiting_manager_approval_WAITING_MANAGER_APPROVAL_waitingManagerApproval_onRelationshipManagerApprovedAction() {
        return new WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_waitingManagerApproval_onRelationshipManagerApproved_ActionImpl();
    }
}