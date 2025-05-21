export * from './eventController.service';
import { EventControllerService } from './eventController.service';
export * from './helloWorldController.service';
import { HelloWorldControllerService } from './helloWorldController.service';
export * from './ticketController.service';
import { TicketControllerService } from './ticketController.service';
export const APIS = [EventControllerService, HelloWorldControllerService, TicketControllerService];
