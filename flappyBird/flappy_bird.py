# Import a library of functions called 'pygame'
import pygame
import random
import math
from math import pi
import pipes as pipeModule
import bird

# Initialize the game engine
pygame.init()

# Define the colors we will use in RGB format
BLACK = (  0,   0,   0)
WHITE = (255, 255, 255)
BLUE =  (  0,   0, 255)
GREEN = (  0, 255,   0)
RED =   (255,   0,   0)

# Set the height and width of the screen
size = [700, 400]
screen = pygame.display.set_mode(size)
pygame.display.set_caption("T flappy birds")

#Loop until the user clicks the close button.
done = False
clock = pygame.time.Clock()
fps = 60
all_sprites = pygame.sprite.Group()

#flappy-bird specific
counter = 0
pipesOnScreen = []
bird = bird.Bird(size[1])

all_sprites.add(bird)

while not done:

    # Clear the screen and set the screen background
    screen.fill(BLACK)

    for event in pygame.event.get(): # User did something
        if event.type == pygame.QUIT:
            pygame.quit()
        # if event.type == pygame.MOUSEBUTTONDOWN: # If user clicked
        #     pos = pygame.mouse.get_pos()
        #     pygame.draw.circle(screen, WHITE, (pos), 20, 1) # Flag that we are done so we exit this loop

    if not pipesOnScreen:
        newPipe = pipeModule.Pipe(size[0], size[1])
        pipesOnScreen.append(newPipe)

    for pipes in pipesOnScreen:
        if((pipes.getXRight() < 0.7*size[0]) and (pipes.lessThanX is False)):
            print("adding new pipe!!")
            pipes.lessThanX = True
            newPipe = pipeModule.Pipe(size[0], size[1])
            pipesOnScreen.append(newPipe)
        if(pipes.getXRight() < 0):
            pipesOnScreen.remove(pipes)

    for pipe in pipesOnScreen:
        currentPipe = pipe
        cpx = currentPipe.getX()
        cpty = currentPipe.getTopY() #current pipe top y
        cpby = currentPipe.getBottomY()

        pygame.draw.rect(screen, WHITE, (cpx, 0, 50, cpty)) #(x, y, width (Top left -> top right), height (top left to bottom))
        pygame.draw.rect(screen, WHITE, (cpx, cpby, 50, size[1] - cpby))
        currentPipe.setX(currentPipe.getX()-2)
        if(currentPipe.hits(bird)):
            print("HIT")

    if(bird.rect.y > size[1]):
        print("DEAD")

    #score counter
    counter += 1
    pygame.font.init()
    myfont = pygame.font.SysFont('Comic Sans MS', 30)
    textsurface = myfont.render("counter :" + str(counter), True, (255, 255, 2))
    screen.blit(textsurface,(50,50))

    #update sprites
    all_sprites.update()

    #draw
    all_sprites.draw(screen)

    #update display
    pygame.display.update()
    clock.tick(fps)

# Be IDLE friendly
pygame.quit()
