package dev.thesummit.rook.data.links.impl

import dev.thesummit.rook.model.Link

val link1 =
    Link(
        /*id=*/ 1,
        /*title=*/ "A sample link",
        /*tags=*/ "example tags",
        /*url=*/ "https://www.google.com",
        /*modified=*/ 1673779779,
    )

val link2 =
    Link(
        /*id=*/ 2,
        /*title=*/ "Yet another sample link",
        /*tags=*/ "example tags",
        /*url=*/ "https://www.google.com",
        /*modified=*/  1673653377,
    )

val link3 =
    Link(
        /*id=*/ 3,
        /*title=*/ "Sample link with a much longer title than normal",
        /*tags=*/ "a couple of tags",
        /*url=*/ "https://www.google.com",
        /*modified=*/ 1673683377,
    )

val link4 =
    Link(
        /*id=*/ 4,
        /*title=*/ "A sample link With lots of tags",
        /*tags=*/ "web other stuff moar tags just give me moar",
        /*url=*/ "https://www.google.com",
        /*modified=*/ 1673693377,
    )

val links: List<Link> = listOf(link1,link2,link3,link4)
