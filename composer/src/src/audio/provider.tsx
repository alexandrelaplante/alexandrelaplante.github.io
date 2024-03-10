import React from "react"
import { Violin } from "./violin"

const violin = new Violin();

export const ViolinContext = React.createContext(violin);


export function ViolinProvider(props: any) {
    return (
        <ViolinContext.Provider value={violin} {...props} />
    );
}