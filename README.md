[![Build Status](https://travis-ci.org/jonghough/ArtisteX.svg?branch=master)](https://travis-ci.org/jonghough/ArtisteX)
# ArtisteX #

Android Graphics Editor

### Description ###

*Proof-of-concept* graphics editor for *Android*. This app lets a user draw lines, shapes, bitmaps / images on a canvas then transform and edit the creations. 
The user interface uses single touches to draw on the screen and edit objects. 



### Functionality ###
| Functionality                   | Implemented |
|---------------------------------|-------------|
| Draw lines                      |  Yes           |
| Draw simple shapes              |  Yes           |
| Writing tool                    |  Not yet       |
| Modify line points              |  Yes           |
| Modify (cubic) bezier points    |  Yes           |
| Add images / photos             |  Yes           |
| Rotate, scale, transform layers |  Yes           |
| Add editable text               |  Yes           |
| Change fill color of layers     |  Yes           |
| Change stroke color of layers     |  Yes           |
| Change stroke caps and join type | Almost        |
| Change stroke thickness         | Yes            |
| Undo features                   |  Yes           |
| Redo features                   |  Not yet       |
| Export canvas as bitmap         |  Yes           |
| Export canvas as SVG            |  Yes           |
| Import SVG                      |  No           |
| Multi-touch / pinch zoom        | Not yet       |
| Copy layers                     | Yes           |

### Design ###
All drawing functionality takes place inside the `DrawingEngine` object, and each layer is responsible for its own drawing functions.

### Example
![gif 1](/aritstex_gif1.gif)

### Tests ###
`./gradlew test`

### Build ###
`./gradlew assembleDebug`
