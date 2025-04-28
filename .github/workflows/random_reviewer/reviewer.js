const core = require("@actions/core");
const github = require("@actions/github");

const member= {
    epdlrnldudnj: "776773942874472450",
    cmj7271: "740776211231277066",
    hwnooy: "1023475844456386632",
    optiprime27: "1082851557361602571"
}

function selectRandomReviewer() {
    const candidate = Object.keys(member).filter(
        (name) => name !== github.context.actor
    )
    return candidate[
        Math.floor(Math.random() * candidate.length)
        ]
}

async function sendDiscordMsg(reviewer, title) {
    const webhook = process.env.DISCORD_WEBHOOK;

    const msg = {
        content: createMsg(reviewer, title)
    }

    await fetch(webhook, {
        method: "POST",
        headers: { 'Content-type': 'application/json' },
        body: JSON.stringify(msg)
    })
}

function createMsg(reviewer, title) {
    return "# " + title + "\n" + "* PR: " + `https://github.com/${github.context.repo.owner}/${github.context.repo.repo}/pull/${github.context.payload.pull_request.number}`
        + "\n* 담당자: " + "<@!" + member[reviewer] + ">"
        + "께서 리뷰를 맡게 되었습니다. :tada:"
}

async function main() {
    const githubClient = github.getOctokit(process.env.REVIEW_TOKEN);
    let reviewer = "";

    const { owner, repo } = github.context.repo;
    const pr_info = {
        owner: owner,
        repo: repo,
        pull_number: github.context.payload.pull_request.number
    }

    const requested_reviewers = await githubClient.rest.pulls.listRequestedReviewers(pr_info)

    if(requested_reviewers.data.users.length === 0) {
        reviewer = selectRandomReviewer();
        console.log("assign reviewer: ", reviewer);

        githubClient.rest.pulls.requestReviewers(
            {
                ...pr_info,
                reviewers: [reviewer]
            }
        )
            .then((res) => console.log("reviewer assign success: ", res))
            .catch((err) => {
                console.log("reviewer assign failed:", err);
                process.exit(1);
            });
    } else {
        console.log("already assigned reviewer exist.")
        reviewer = requested_reviewers.data.users[0].login;
    }

    const pr = await githubClient.rest.pulls.get(
        {
            owner: owner,
            repo: repo,
            pull_number: github.context.payload.pull_request.number
        }
    )

    sendDiscordMsg(reviewer, pr.data.title)
        .then(() => console.log("message send success"))
        .catch((err) => {
            console.log("message send failed");
            core.setFailed(err.message);
        });
}

main().then(() => console.log("success")).catch((err) => {
    console.log("failed")
    core.setFailed(err.message);
});