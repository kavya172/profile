package com.galaxe.gxworkflow.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The Constants class contains various constant values related to workflow.
 *
 */
public class Constants {

	// FIXME: WHY CONSTANTS CLASS IS IN UTIL PACKAGE. MOVE IT UNDER CONSTANTS
	// PACKAGE.

	public static final String PROCESS = "Process";
	public static final String START_EVENT = "bpmn:startEvent";
	public static final String END_EVENT = "bpmn:endEvent";
	public static final String OUTGOING = "Outgoing";
	public static final String INCOMING = "Incoming";
	public static final String TASK = "Task";
	public static final String USER_TASK = "bpmn:userTask";
	public static final String SERVICE_TASK = "bpmn:serviceTask";
	public static final String SCRIPT_TASK = "bpmn:scriptTask";
	public static final String SEND_TASK = "bpmn:sendTask";
	public static final String SEQUENCE_FLOW_TASK = "bpmn:sequenceFlow";
	public static final String BPMN_DIAGRAMN = "bpmndi:bpmndiagram";
	public static final String RECEIVE_TASK = "ReceiveTask";
	public static final String MANUAL_TASK = "ManualTask";
	public static final String BUSINESS_RULE_TASK = "BusinessRuleTask";
	public static final String SEQUENCE_FLOW = "SequenceFlow";
	public static final String MESSAGE_EVENT_DEFINITION = "MessageEventDefinition";
	public static final String TIMER_EVENT_DEFINITION = "TimerEventDefinition";
	public static final String CONDITIONAL_EVENT_DEFINITION = "ConditionalEventDefinition";
	public static final String LINK_EVENT_DEFINITION = "LinkEventDefinition";
	public static final String ESCALATION_EVENT_DEFINITION = "EscalationEventDefinition";
	public static final String ERROR_EVENT_DEFINITION = "ErrorEventDefinition";
	public static final String COMPENSATE_EVENT_DEFINITION = "CompensateEventDefinition";
	public static final String TERMINATE_EVENT_DEFINITION = "TerminateEventDefinition";
	public static final String DATA_STORE_REFERENCE = "DataStoreReference";
	public static final String DATA_OBJECT = "DataObject";
	public static final String DATA_OBJECT_REFERENCE = "DataObjectReference";
	public static final String SIGNAL_EVENT_DEFINITION = "SignalEventDefinition";
	public static final String INTERMEDIATE_THROW_EVENT = "IntermediateThrowEvent";
	public static final String INTERMEDIATE_CATCH_EVENT = "IntermediateCatchEvent";
	public static final String EXCLUSIVE_GATEWAY = "bpmn:exclusiveGateway";
	public static final String PARALLEL_GATEWAY = "ParallelGateway";
	public static final String EVENT_BASED_GATEWAY = "EventBasedGateway";
	public static final String SUB_PROCESS = "SubProcess";
	public static final String TRANSACTION = "Transaction";
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String INPUT = "input";
	public static final String TYPE = "type";
	public static final String NAMESPACE = "http://custom.com/ns";
	public static final String SOURCEREF = "sourceRef";
	public static final String TARGETREF = "targetRef";
	public static final String CALL_ACTIVITY = "CallActivity";
	public static final int EXECUTABLE = 1;
	public static final int NOT_EXECUTABLE = 0;
	public static final String VERSION = "version";
	public static final String VERSION_NAME = "versionName";
	public static final String ALLUSERS = "allUsers";
	public static final String ROLE = "role";

	public static final String ISEXECUTABLE = "isExecutable";

	public static final String INPROGRESS = "INPROGRESS";
	public static final String COMPLETED = "COMPLETED";

	public static final Set<String> automaticTasks = new HashSet<>(Arrays.asList(""));

	public static final String ELEMENT_PREFIX = "gxw";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BASICAUTH = "BasicAuth";

//	ScriptTask constants

	public static final String QUERY = "Query";
	public static final String PROCEDURE = "Procedure";
	public static final String FUNCTION = "Function";
	public static final String PROCEDURAL_LANGUAGE = "Procedural Language";

	public static final String DATABASE = "database";
	public static final String JAVASCRIPT = "javascript";
	public static final String SELECT = "Select";

	public static final String HEADER_PARAM = "headerParam";
	public static final String QUERY_PARAM = "queryParam";
	public static final String SERVICE_INTEGRATION = "serviceIntegration";
	public static final String OUTPUT = "output";

//	Database connection

	public static final String SQLSERVERDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static final String MYSQLDRIVER = "com.mysql.jdbc.Driver";
	public static final String POSTGRESSQLDRIVER = "org.postgresql.Driver";
	public static final String MONGODBDRIVER = "mongodb.jdbc.MongoDriver";

	public static final String SQLSERVER = "MS SQL Server";
	public static final String POSTGRESSQL = "PostgreSQL";
	public static final String MYSQLSERVER = "MySQL";
	public static final String MONGODB = "MongoDB";

	// Send Task
	public static final String EMAIL_PROPERTIES = "emailProperties";
	public static final String SUBJECT = "subject";
	public static final String BODY = "body";
	public static final String BODY_TyPE = "bodyType";

	public static final String INPUT_VARIABLE = "inputVariable";
	public static final String OUTPUT_VARIABLE = "outputVariable";
	public static final String CUSTOM_INPUT_VARIABLE = "gxw:inputVariable";
	public static final String CUSTOM_USER_DETAILS = "gxw:userDetails";
	public static final String EXTENSION_ELEMENTS = "bpmn:extensionElements";

	public static final String CONDITIONAL_EXPRESSION = "conditionExpression";
	public static final String POSSIBLE_ACTIONS = "possibleActions";
	public static final String CONDITIONAL = "conditional";

	public static final String COLOR_PROPERTIES = "colorProperties";
	public static final String COLOR = "color";
	public static final String DEFAULT_COLOR = "DefaultColor";
	public static final String SUCCESS_COLOR = "SuccessColor";
	public static final String FAILURE_COLOR = "FailureColor";

//	Service Integration
	public static final String GET="GET";
	public static final String POST="POST";
	public static final String PUT="PUT";
	public static final String DELETE="DELETE";

}
