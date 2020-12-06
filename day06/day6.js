const fs = require('fs');

fs.readFile('input', 'utf8' , (err, data) => {
    const part1 = data.split("\n\n")
        .map(answers => answers.replace(/\n/g, ""))
        .map((answers) => {
            return new Set(answers).size;
        })
        .reduce((sum, answers) => sum + answers, 0);

    const part2 = data.split("\n\n")
        .map(answers => answers.split("\n"))
        .map((answers) => {
            return answers
                .map(answer => new Set(answer))
                .reduce((commonAnswers, answers) =>
                    new Set([...commonAnswers]
                        .filter(answer => answers.has(answer))));
        })
        .reduce((sum, answers) => sum + answers.size, 0);

    console.log(part1);
    console.log(part2);
})
