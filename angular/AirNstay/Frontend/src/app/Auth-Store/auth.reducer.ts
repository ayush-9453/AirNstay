import { createReducer , on } from "@ngrx/store";
import { getUserId } from "./auth.action";

export interface authInterface {
    userId : string | null ;
}

export const initialUserId : authInterface = {
    userId : null,
};

export const authProfileReducer = createReducer(
    initialUserId,
    on(getUserId , (state  , {userId}) => ({...state , userId})),
)