def compare_files(output_file, golden_file):
    with open(output_file, 'r') as f1, open(golden_file, 'r') as f2:
        output_lines = f1.readlines()
        golden_lines = f2.readlines()

        if len(output_lines) != len(golden_lines):
            print("Files have different number of lines!")
            return

        for i, (line1, line2) in enumerate(zip(output_lines, golden_lines)):
            if line1 != line2:
                print(f"Difference found on line {i + 1}:")
                print(f"Output: {line1.strip()}")
                print(f"Golden: {line2.strip()}")

if __name__ == "__main__":
    compare_files("COMP22111/ALU_test_out.txt", "/netopt/info/courses/COMP22111/simfiles/ALU_test_out.txt")
