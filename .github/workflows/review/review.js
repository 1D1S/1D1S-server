const core = require("@actions/core");
const github = require("@actions/github");

const member= {
    epdlrnldudnj: "776773942874472450",
    cmj7271: "740776211231277066",
    hwnooy: "1023475844456386632",
    optiprime27: "1082851557361602571"
}

async function sendDiscordMsg(reviewer, title, status, content) {
    const webhook = process.env.DISCORD_WEBHOOK;

    const msg = {
        content: createMsg(reviewer, title, status, content)
    }

    await fetch(webhook, {
        method: "POST",
        headers: { 'Content-type': 'application/json' },
        body: JSON.stringify(msg)
    })
}

function createMsg(reviewer, title, status, content) {
    return title + ": " + status +
        "\n" + "* PR: " + `https://github.com/${github.context.repo.owner}/${github.context.repo.repo}/pull/${github.context.payload.pull_request.number}`
        + "\n* 담당자: " + "<@!" + member[reviewer] + ">"
        + "께서 리뷰를 남겼습니다.\n"
        + content;

}

async function main() {
    const githubClient = github.getOctokit(process.env.REVIEW_TOKEN);

    const { owner, repo } = github.context.repo;
    const { data: reviews } = await githubClient.rest.pulls.listReviews({
        owner: owner,
        repo: repo,
        pull_number: github.context.payload.pull_request.number
    });

    const lastReview = reviews[reviews.length - 1];
    if(lastReview === undefined) {
        console.log("no review found");
        core.setFailed("no review found");
        return;
    }

    const title = "# [review num \#" + lastReview.id + "](" + lastReview.html_url + ")";
    sendDiscordMsg(member[lastReview.user], title, lastReview.state, lastReview.body)
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