import { SymbolProps } from "./types";

export function Line({ x, y }: SymbolProps) {
    return (
        <g transform={`translate(${x + 106}, ${y + 38.2})`}>
            <path d="m 0,98 0,-2 25,0 25,0 0,2 0,2 -25,0 -25,0 0,-2 z" />
        </g>
    );
}
