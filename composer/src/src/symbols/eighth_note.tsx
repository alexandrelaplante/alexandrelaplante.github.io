import { SymbolProps } from "./types"

export function EighthNote({x, y}: SymbolProps) {
    return (
        <g transform={`translate(${x-110}, ${y+8})`}>
            <path d="m 254.30279,43.363271 0,74.531249 c -2.19436,-3.1329 -11.11823,-3.60474 -17.375,-0.15625 -7.41543,4.0871 -10.79756,11.75107 -7.5625,17.125 3.23507,5.37393 11.86582,6.43085 19.28125,2.34375 4.45154,-2.45352 7.41871,-6.2048 8.40625,-9.96875 0.45244,-2.06878 0.18854,-3.14209 0.15625,-4.59375 l 0,-52.656249 c 32.53399,8.241492 17.51919,36.811079 14.59268,46.902749 26.72779,-36.505741 -14.22819,-51.395935 -14.59268,-73.527749 z" />
        </g>
    )
}