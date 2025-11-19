import { createAction, props } from "@ngrx/store";


export const getUserId = createAction("UserId is Stored" , props<{userId : string | null}>() );