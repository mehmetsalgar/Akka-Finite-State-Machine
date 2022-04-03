package org.salgar.fsm.akka.foureyes.credit.actions.config;

import org.salgar.akka.fsm.foureyes.cra.kafka.CustomerRelationshipAdapter;
import org.salgar.akka.fsm.foureyes.notifier.NotifierService;
import org.salgar.fsm.akka.foureyes.addresscheck.facade.AdressCheckSMFacade;
import org.salgar.fsm.akka.foureyes.credit.actions.*;
import org.salgar.fsm.akka.foureyes.creditscore.facade.MultiTenantCreditScoreSMFacade;
import org.salgar.fsm.akka.foureyes.fraudprevention.facade.FraudPreventionSMFacade;
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
    public CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_ACCEPTED_manager_onAccepted_Action
        creditsm_credit_application_submitted_CREDIT_ACCEPTED_manager_onAcceptedAction(NotifierService notifierService) {
        return new CREDIT_APPLICATION_SUBMITTED_$$_CREDIT_ACCEPTED_manager_onAccepted_ActionImpl(notifierService);
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
}