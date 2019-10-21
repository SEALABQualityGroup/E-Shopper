from locust import HttpLocust, TaskSet, task

class WebsiteTasks(TaskSet):
    
    @task
    def index(self):
        self.client.get("/")
        
class WebsiteUser(HttpLocust):
    task_set = WebsiteTasks
    min_wait = 50
    max_wait = 50
