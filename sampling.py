#coding=utf-8
import requests
import csv

def find_test_directory(github_username, github_repo):
    username = github_username
    repo = github_repo
    # build api url
    api_url = "https://api.github.com/repos/" + username + "/" + repo + "/contents"
    # GET
    response = requests.get(api_url)
    if response.status_code == 200:
        #parse api response
        repo_contents = response.json()
        # traverse and find test directory
        test_directory = None
        for item in repo_contents:
            if item["type"] == "dir":
                if "test" in item["name"].lower():
                    test_directory = item["name"]
                    break
                elif "src" in item["name"].lower():
                    html_url = item["html_url"]
                    html_response = requests.get(html_url)
                    if html_response.status_code == 200:
                        html_json = html_response.json()
                        print(html_json)
                        if "src/test" in html_json:
                            test_directory = "test"
                            break
                    else:
                        print("network error code: ", html_response.status_code)
                        break
        print(test_directory)
        if test_directory:
            print("locate at: ", test_directory)
        else:
            print("no findings")
    else:
        print("error code ", response.status_code)


def read_files(filename):
    with open(filename, 'r') as csvfile:
        csvreader = csv.reader(csvfile)
    for row in csvreader:
        name = row[1]
        print("projects name: ",name)
        find_test_directory(github_username, github_repo)

if __name__ == "__main__":
    github_username = "compomics"
    github_repo = "compomics-utilities"
    filename = "projects.csv"
    read_files(filename)
