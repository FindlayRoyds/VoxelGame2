import sys
from PIL import Image

def add_alpha_channel(input_filename):
    # Open the RGB image
    rgba_image = Image.open(input_filename + ".png").convert("RGBA")

    # Create a new image with fully opaque alpha values
    rgba_with_alpha = Image.new("RGBA", rgba_image.size, (255, 255, 255, 255))

    # Composite the original RGBA image onto the new image
    rgba_with_alpha.paste(rgba_image, (0, 0), rgba_image)

    # Save the resulting image, overwriting the original file
    rgba_with_alpha.save(input_filename + ".png")

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python add_alpha_channel.py <filename_without_extension>")
        sys.exit(1)

    filename = sys.argv[1]
    add_alpha_channel(filename)
