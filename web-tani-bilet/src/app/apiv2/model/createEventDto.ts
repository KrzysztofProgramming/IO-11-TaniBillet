/**
 * OpenAPI definition
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

export interface CreateEventDto { 
    name: string;
    eventStartTimeMillis: Date;
    eventEndTimeMillis: Date;
    location: string;
    ticketPrice: number;
    maxTicketCount: number;
    description: string;
    isBuyingTicketsTurnedOff: boolean;
    eventType: CreateEventDto.EventTypeEnum;
}
export namespace CreateEventDto {
    export type EventTypeEnum = 'CONCERT' | 'CONFERENCE' | 'WORKSHOP' | 'MEETUP' | 'FESTIVAL';
    export const EventTypeEnum = {
        CONCERT: 'CONCERT' as EventTypeEnum,
        CONFERENCE: 'CONFERENCE' as EventTypeEnum,
        WORKSHOP: 'WORKSHOP' as EventTypeEnum,
        MEETUP: 'MEETUP' as EventTypeEnum,
        FESTIVAL: 'FESTIVAL' as EventTypeEnum
    };
}