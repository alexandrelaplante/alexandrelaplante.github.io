import { SymbolProps } from "./types"

export function Dot({x, y}: SymbolProps) {
    return (
        <g transform={`translate(${x+40}, ${y-2.5})`}>
            <path d="m 125.22407,137.77208 c 0,2.40431 -1.94908,4.35339 -4.35339,4.35339 -2.40431,0 -4.35339,-1.94908 -4.35339,-4.35339 0,-2.40431 1.94908,-4.35339 4.35339,-4.35339 2.40431,0 4.35339,1.94908 4.35339,4.35339 z" />
        </g>
    )
}