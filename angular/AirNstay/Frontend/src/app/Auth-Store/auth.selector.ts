import { createFeatureSelector, createSelector } from "@ngrx/store";
import { authInterface } from "./auth.reducer";


export const authSelector = createFeatureSelector<authInterface>('userId');

export const storeUserId = createSelector(
    authSelector,
    (state : authInterface) => state.userId
) 

