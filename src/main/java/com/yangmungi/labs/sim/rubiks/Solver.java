package com.yangmungi.labs.sim.rubiks;

/**
 * Created by Yangmun on 6/1/2016.
 */
public class Solver {
    public static void main(String[] args) {
        /*
        The first surface (+x) can be any 6 surfaces.
          This forces the opposite surface (-x).
        The next surface can be any of remaining 4 surfaces.
          This forces its opposite surface.
          This forces proximity surface, i.e. if 4 surfaces have been determined,
            the final two surfaces are predetermined, given consistent surface
            identification.
        This results in 24 possible rotations.

        For any possible rotation, one can turn the (+y) surface.

                (+z)   (-x)
                 |   /
                 | /
        (-y) - -   - - (+y)
               / |
             /   |
        (+x)    (-z)

        Default Layout:
            0 -> +x
            1 -> +y
            2 -> +z
            3 -> -x
            4 -> -y
            5 -> -z

        26 Pieces, 6 positions (3 bits), means 72 bits.
        9 bytes per position.

        A rejected state representation is by keeping track of each surface.
        There are a total of 54 surfaces
          8 corners, 3 surfaces = 24
          6 inners, 1 surface   = 6
          12 edges, 2 surfaces  = 24

        Each surface can be one of 6 colors (3 bits).

        All 26 pieces
         */


    }
}
